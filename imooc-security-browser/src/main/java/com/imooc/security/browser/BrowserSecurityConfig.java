package com.imooc.security.browser;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeSecurityConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import static com.imooc.security.core.properties.SecurityConstants.*;

/**
 * 浏览器安全配置。
 *
 * @author DENGBIN
 * @since 2018-4-15
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    /**
     * 安全配置属性。
     */
    private final SecurityProperties securityProperties;
    /**
     * 数据源。
     */
    private final DataSource dataSource;
    /**
     * 用户详情服务。
     */
    private final UserDetailsService userDetailsService;
    /**
     * 验证码安全配置。
     */
    private final ValidateCodeSecurityConfig validateCodeSecurityConfig;
    /**
     * 短信验证码安全配置。
     */
    private final SmsCodeSecurityConfig smsCodeSecurityConfig;

    @Autowired
    public BrowserSecurityConfig(SecurityProperties securityProperties,
                                 UserDetailsService userDetailsService,
                                 ValidateCodeSecurityConfig validateCodeSecurityConfig,
                                 SmsCodeSecurityConfig smsCodeSecurityConfig,
                                 DataSource dataSource) {
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
        this.validateCodeSecurityConfig = validateCodeSecurityConfig;
        this.smsCodeSecurityConfig = smsCodeSecurityConfig;
        this.dataSource = dataSource;
    }

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)
                .and()
            .apply(smsCodeSecurityConfig)
                .and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
            .authorizeRequests()
                .antMatchers(
                    DEFAULT_UNAUTHENTICATION_URL,
                    DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                    securityProperties.getBrowser().getLoginPage(),
                    DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                    "/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf().disable();
    }
    // @formatter:on

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
