package com.imooc.security.core.validate.code.image;

import com.imooc.security.core.validate.code.ValidateCode;
import lombok.Getter;
import lombok.ToString;

import java.awt.image.BufferedImage;

/**
 * 图片验证码。
 *
 * @author DENGBIN
 * @since 2018-4-12
 */
@Getter
@ToString
public class ImageCode extends ValidateCode {

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }
}
