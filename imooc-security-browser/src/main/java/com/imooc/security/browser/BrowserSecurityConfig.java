package com.imooc.security.browser;

import com.imooc.security.browser.authentication.ImoocAuthenticationFailureHandler;
import com.imooc.security.browser.authentication.ImoocAuthenticationSuccessHandler;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.SmsCodeFilter;
import com.imooc.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author DENGBIN
 * @since 2018-4-15
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ImoocAuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
    private final ImoocAuthenticationFailureHandler imoocAuthenticationFailureHandler;
    private final SecurityProperties securityProperties;
    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    public BrowserSecurityConfig(ImoocAuthenticationSuccessHandler imoocAuthenticationSuccessHandler,
                                 ImoocAuthenticationFailureHandler imoocAuthenticationFailureHandler,
                                 SecurityProperties securityProperties,
                                 DataSource dataSource,
                                 @Qualifier("myUserDetailsService") UserDetailsService userDetailsService,
                                 SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig) {
        this.imoocAuthenticationSuccessHandler = imoocAuthenticationSuccessHandler;
        this.imoocAuthenticationFailureHandler = imoocAuthenticationFailureHandler;
        this.securityProperties = securityProperties;
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
    }

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();

        http
            .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                .loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/form")
                .successHandler(imoocAuthenticationSuccessHandler)
                .failureHandler(imoocAuthenticationFailureHandler)
            .and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
            .and()
            .authorizeRequests()
                .antMatchers("/authentication/require",
                    securityProperties.getBrowser().getLoginPage(),
                    "/code/*",
                    "/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
            .and().csrf().disable()
            .apply(smsCodeAuthenticationSecurityConfig);
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
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }
}
