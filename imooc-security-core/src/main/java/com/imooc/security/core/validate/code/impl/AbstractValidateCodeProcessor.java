package com.imooc.security.core.validate.code.impl;

import com.imooc.security.core.validate.code.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX;

/**
 * 验证码抽象处理器。
 * 子类需要实现发送验证码的方法。
 *
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
        return StringUtils.substringAfter(request.getRequestURI(), DEFAULT_VALIDATE_CODE_URL_PREFIX + "/");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(HttpServletRequest request) {
        ValidateCodeType processorType = getValidateCodeType();
        String sessionKey = getSessionKey();

        C codeInSession = (C) request.getSession().getAttribute(sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpired()) {
            request.getSession().removeAttribute(sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        request.getSession().removeAttribute(sessionKey);
    }

    /**
     * 根据请求的url获取校验码的类型。
     *
     * @return 验证码类型
     */
    private ValidateCodeType getValidateCodeType() {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 返回验证码放入session时的key。
     *
     * @return SESSION KEY
     */
    private String getSessionKey() {
        return SESSION_KEY_PREFIX + getValidateCodeType().toString().toUpperCase();
    }
}
