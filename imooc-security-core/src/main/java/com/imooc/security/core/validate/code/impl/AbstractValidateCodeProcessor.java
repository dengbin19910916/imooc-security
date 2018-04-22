package com.imooc.security.core.validate.code.impl;

import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.ValidateCodeGenerator;
import com.imooc.security.core.validate.code.ValidateCodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author DENGBIN
 * @since 2018-4-19
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, response, validateCode);
    }

    /**
     * 生产校验码。
     *
     * @param request 请求对象
     * @return 校验码
     */
    @SuppressWarnings("unchecked")
    private C generate(HttpServletRequest request) {
        String type = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(type + "CodeGenerator");
        return (C) validateCodeGenerator.generate(request);
    }

    /**
     * 保存校验码。
     *
     * @param request      请求对象
     * @param validateCode 校验码
     */
    private void save(HttpServletRequest request, C validateCode) {
        request.getSession().setAttribute(SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase(), validateCode);
    }

    /**
     * 发送校验码，由子类实现。
     *
     * @param request      请求对象
     * @param validateCode 验证码
     * @throws Exception 程序异常
     */
    protected abstract void send(HttpServletRequest request, HttpServletResponse response, C validateCode) throws Exception;

    /**
     * 根据请求的URL获取校验码的类型。
     *
     * @param request 请求对象
     * @return 校验码的类型
     */
    private String getProcessorType(HttpServletRequest request) {
        return StringUtils.substringAfter(request.getRequestURI(), "/code/");
    }
}
