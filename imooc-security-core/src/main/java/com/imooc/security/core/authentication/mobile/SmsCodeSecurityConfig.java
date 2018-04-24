package com.imooc.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信验证码安全配置。
 *
 * @author DENGBIN
 * @since 2018-4-22
 */
@Component
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 认证成功处理器。
     */
    private final AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
    /**
     * 认证失败处理器。
     */
    private final AuthenticationFailureHandler imoocAuthenticationFailureHandler;
    /**
     * 用户明细服务。
     */
    private final UserDetailsService userDetailsService;

    @Autowired
    public SmsCodeSecurityConfig(AuthenticationSuccessHandler imoocAuthenticationSuccessHandler, AuthenticationFailureHandler imoocAuthenticationFailureHandler, @Qualifier("myUserDetailsService") UserDetailsService userDetailsService) {
        this.imoocAuthenticationSuccessHandler = imoocAuthenticationSuccessHandler;
        this.imoocAuthenticationFailureHandler = imoocAuthenticationFailureHandler;
        this.userDetailsService = userDetailsService;
    }

    // @formatter:off
    @Override
    public void configure(HttpSecurity http) {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(imoocAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
            .addFilterBefore(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    // @formatter:on
}
