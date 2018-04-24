package com.imooc.security.core.validate.code.sms;

/**
 * @author DENGBIN
 * @since 2018-4-19
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机 " + mobile + " 发送验证码 " + code);
    }
}
