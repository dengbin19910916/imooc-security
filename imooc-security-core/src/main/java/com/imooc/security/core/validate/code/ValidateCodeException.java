package com.imooc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常。
 *
 * @author DENGBIN
 * @since 2018-4-13
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
