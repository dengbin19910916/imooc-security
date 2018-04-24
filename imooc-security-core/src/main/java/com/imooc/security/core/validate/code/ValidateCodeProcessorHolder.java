package com.imooc.security.core.validate.code;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 系统中的校验码处理器。
 *
 * @author DENGBIN
 * @since 2018-4-22
 */
@Component
public class ValidateCodeProcessorHolder {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 查找系统中的校验码处理器。
     *
     * @param validateCodeType 验证码类型
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType validateCodeType) {
        return findValidateCodeProcessor(validateCodeType.toString().toLowerCase());
    }

    /**
     * 查找系统中的校验码处理器。
     *
     * @param validateCodeType 验证码类型
     * @return 验证码处理器
     */
    private ValidateCodeProcessor findValidateCodeProcessor(String validateCodeType) {
        String name = validateCodeType.toLowerCase() + StringUtils.substringAfter(ValidateCodeProcessor.class.getSimpleName(), "Validate");
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }
}
