package com.imooc.security.core.validate.code;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author DENGBIN
 * @since 2018-4-12
 */
@Getter
@ToString
public class ValidateCode {

    protected String code;

    protected LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
