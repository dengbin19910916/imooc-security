package com.imooc.security.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM;
import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_UNAUTHENTICATION_URL;

/**
 * 权限用户密码登录配置。
 *
 * @author DENGBIN
 * @since 2018-4-22
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    // @formatter:off
    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginPage(DEFAULT_UNAUTHENTICATION_URL)
            .loginProcessingUrl(DEFAULT_LOGIN_PROCESSING_URL_FORM)
            .successHandler(imoocAuthenticationSuccessHandler)
            .failureHandler(imoocAuthenticationFailureHandler);
    }
    // @formatter:on
}
