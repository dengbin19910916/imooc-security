package com.imooc.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现验证码接口提供自定义的验证码生成方式。
 *
 * @author DENGBIN
 * @since 2018-4-18
 */
public interface ValidateCodeGenerator {

    /**
     * 生成验证码。
     *
     * @param request 请求对象
     * @return 验证码
     */
    ValidateCode generate(HttpServletRequest request);
}
