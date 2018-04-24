package com.imooc.security.core.validate.code;

/**
 * @author DENGBIN
 * @since 2018-4-13
 */
//@Slf4j
//@Component
//public class SmsCodeFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private AuthenticationFailureHandler authenticationFailureHandler;
//
//    private Set<String> urls = new HashSet<>();
//
//    @Autowired
//    private SecurityProperties securityProperties;
//
//    private AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(), ",");
//        if (configUrls != null) {
//            Collections.addAll(urls, configUrls);
//        }
//        urls.add("/authentication/mobile");
//    }
//
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//        boolean action = false;
//        for (String url : urls) {
//            if (pathMatcher.match(url, request.getRequestURI())) {
//                action = true;
//                break;
//            }
//        }
//
//        if (action) {
//            try {
//                validate(request);
//            } catch (ValidateCodeException e) {
//                log.error(e.getMessage());
//                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
//        ValidateCode codeInSession = (ValidateCode) request.getSession().getAttribute(SESSION_KEY_PREFIX + "SMS");
//
//        String codeInRequest = ServletRequestUtils.getStringParameter(request, "smsCode");
//
//        if (StringUtils.isBlank(codeInRequest)) {
//            throw new ValidateCodeException("短信验证码的值不能为空");
//        }
//
//        if (codeInSession == null) {
//            throw new ValidateCodeException("短信验证码不存在");
//        }
//
//        if (codeInSession.isExpired()) {
//            request.getSession().removeAttribute(SESSION_KEY_PREFIX + "SMS");
//            throw new ValidateCodeException("短信验证码已过期");
//        }
//
//        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
//            throw new ValidateCodeException("短信验证码不匹配");
//        }
//
//        request.getSession().removeAttribute(SESSION_KEY_PREFIX + "SMS");
//    }
//}
