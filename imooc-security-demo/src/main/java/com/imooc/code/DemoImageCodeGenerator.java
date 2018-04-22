package com.imooc.code;

import com.imooc.security.core.validate.code.ValidateCodeGenerator;
import com.imooc.security.core.validate.code.image.ImageCode;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DENGBIN
 * @since 2018-4-18
 */
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ImageCode generate(HttpServletRequest request) {
        System.out.println("自定义的图像验证码");
        return null;
    }
}
