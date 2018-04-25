package com.imooc.security.core.validate.code;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 验证码。
 *
 * @author DENGBIN
 * @since 2018-4-12
 */
@Getter
@ToString
public class ValidateCode {

    /**
     * 验证码。
     */
    protected String code;
    /**
     * 过期日期。
     */
    protected LocalDateTime expireTime;

    /**
     * 创建验证码对象。
     *
     * @param code     验证码
     * @param expireIn 过期时间（秒）
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 验证码是否过期。
     *
     * @return true - 已过期，false - 未过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
