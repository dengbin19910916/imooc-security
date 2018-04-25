package com.imooc.security.core.validate.code.sms;

/**
 * 实现此接口提供自定义的短信验证码发送实现。
 *
 * @author DENGBIN
 * @since 2018-4-19
 */
public interface SmsCodeSender {

    void send(String mobile, String code);
}
