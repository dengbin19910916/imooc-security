package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM;
import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE;
import static com.imooc.security.core.validate.code.ValidateCodeType.IMAGE;
import static com.imooc.security.core.validate.code.ValidateCodeType.SMS;

/**
 * 验证码处理过滤器。
 *
 * @author DENGBIN
 * @since 2018-4-13
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final SecurityProperties securityProperties;
    private final ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的url。
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的工具类。
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler, SecurityProperties securityProperties, ValidateCodeProcessorHolder validateCodeProcessorHolder) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.securityProperties = securityProperties;
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
    }

    /**
     * 初始化要拦截的url配置信息。
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(DEFAULT_LOGIN_PROCESSING_URL_FORM, IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), IMAGE);

        urlMap.put(DEFAULT_LOGIN_PROCESSING_URL_MOBILE, SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), SMS);
    }

    /**
     * 讲系统中配置的需要校验验证码的URL根据校验的类型放入map。
     *
     * @param urlString        url配置
     * @param validateCodeType 验证码类型
     */
    private void addUrlToMap(String urlString, ValidateCodeType validateCodeType) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, validateCodeType);
            }
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeType(request);
        if (type != null) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(request);
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 获取验证码的类型，如果当前请求不需要校验，则返回null。
     *
     * @param request 请求对象
     * @return 验证码类型
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType type = null;
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    type = urlMap.get(url);
                }
            }
        }
        return type;
    }
}
