package com.imooc.security.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Imooc security properties.
 *
 * @author DENGBIN
 * @since 2018-4-11
 */
@Data
@ConfigurationProperties(prefix = "imooc.security")
public class SecurityProperties {

    /**
     * Return browser security properties.
     */
    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    /**
     * Imooc security browser properties.
     */
    @Data
    public class BrowserProperties {

        private String loginPage = "/imooc-signIn.html";

        private LoginType loginType = LoginType.JSON;

        private int rememberMeSeconds = 3600;
    }

    @Data
    public class ValidateCodeProperties {

        private ImageCodeProperties image = new ImageCodeProperties();

        private SmsCodeProperties sms = new SmsCodeProperties();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public class ImageCodeProperties extends SmsCodeProperties {
        private int width = 67;
        private int height = 23;

        public ImageCodeProperties() {
            setLength(4);
        }
    }

    @Data
    public class SmsCodeProperties {
        private int length = 6;
        private int expireIn = 60;
        private String url;
    }
}
