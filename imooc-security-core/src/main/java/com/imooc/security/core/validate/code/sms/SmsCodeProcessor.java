package com.imooc.security.core.validate.code.sms;

import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.validate.code.ValidateCode;
import com.imooc.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信验证码处理器。
 *
 * @author DENGBIN
 * @since 2018-4-20
 */
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 短信验证码发送器。
     */
    private final SmsCodeSender smsCodeSender;

    @Autowired
    public SmsCodeProcessor(SmsCodeSender smsCodeSender) {
        this.smsCodeSender = smsCodeSender;
    }

    @Override
    protected void send(HttpServletRequest request, HttpServletResponse response, ValidateCode validateCode) throws Exception {
        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, paramName);
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
