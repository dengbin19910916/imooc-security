package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.imooc.security.core.validate.code.ValidateCodeProcessor.SESSION_KEY_PREFIX;

/**
 * @author DENGBIN
 * @since 2018-4-13
 */
@Slf4j
@NoArgsConstructor
public class SmsCodeFilter extends OncePerRequestFilter {

    @Setter
    private AuthenticationFailureHandler authenticationFailureHandler;

    private Set<String> urls = new HashSet<>();

    @Setter
    private SecurityProperties securityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(), ",");
        if (configUrls != null) {
            Collections.addAll(urls, configUrls);
        }
        urls.add("/authentication/mobile");
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        boolean action = false;
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }

        if (action) {
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                log.error(e.getMessage());
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        ValidateCode codeInSession = (ValidateCode) request.getSession().getAttribute(SESSION_KEY_PREFIX + "SMS");

        String codeInRequest = ServletRequestUtils.getStringParameter(request, "smsCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            request.getSession().removeAttribute(SESSION_KEY_PREFIX + "SMS");
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        request.getSession().removeAttribute(SESSION_KEY_PREFIX + "SMS");
    }
}
