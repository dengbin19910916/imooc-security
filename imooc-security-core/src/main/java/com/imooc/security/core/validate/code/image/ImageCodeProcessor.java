package com.imooc.security.core.validate.code.image;

import com.imooc.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author DENGBIN
 * @since 2018-4-20
 */
@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 发送图形验证码，将其写到响应中。
     */
    @Override
    protected void send(HttpServletRequest request, HttpServletResponse response, ImageCode imageCode) throws IOException {
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
}
