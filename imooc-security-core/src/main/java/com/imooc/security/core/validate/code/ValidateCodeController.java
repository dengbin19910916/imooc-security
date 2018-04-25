package com.imooc.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.imooc.security.core.properties.SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX;

/**
 * 验证码创建控制器。
 *
 * @author DENGBIN
 * @since 2018-4-12
 */
@RestController
public class ValidateCodeController {

    private final Map<String, ValidateCodeProcessor> validateCodeProcessors;

    @Autowired
    public ValidateCodeController(Map<String, ValidateCodeProcessor> validateCodeProcessors) {
        this.validateCodeProcessors = validateCodeProcessors;
    }

    @GetMapping(DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        validateCodeProcessors.get(type + "CodeProcessor").create(request, response);
    }
}
