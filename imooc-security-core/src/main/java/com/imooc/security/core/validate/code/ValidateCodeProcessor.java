package com.imooc.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DENGBIN
 * @since 2018-4-19
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀。
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建验证码。
     *
     * @param request 请求对象
     */
    void create(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 校验验证码。
     *
     * @param request 请求对象
     */
    void validate(HttpServletRequest request);
}
