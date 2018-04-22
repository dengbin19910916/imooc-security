package com.imooc.security.core.validate.code.sms;

/**
 * @author DENGBIN
 * @since 2018-4-19
 */
public interface SmsCodeSender {

    void send(String mobile, String code);
}
