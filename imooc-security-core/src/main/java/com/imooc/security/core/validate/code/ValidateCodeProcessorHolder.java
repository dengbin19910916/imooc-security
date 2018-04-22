package com.imooc.security.core.validate.code;

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

    private final Map<String, ValidateCodeProcessor> validateCodeProcessors;

    @Autowired
    public ValidateCodeProcessorHolder(Map<String, ValidateCodeProcessor> validateCodeProcessors) {
        this.validateCodeProcessors = validateCodeProcessors;
    }

    /**
     * 查找系统中的校验码处理器。
     *
     * @param validateCodeType 验证码类型
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType validateCodeType) {
        return validateCodeProcessors.get(validateCodeType.toString().toLowerCase());
    }

    /**
     * 查找系统中的校验码处理器。
     *
     * @param validateCodeType 验证码类型
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String validateCodeType) {
        String name = validateCodeType.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }
}
