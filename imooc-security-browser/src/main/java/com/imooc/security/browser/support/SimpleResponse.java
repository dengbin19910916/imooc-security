package com.imooc.security.browser.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 响应对象。
 *
 * @author DENGBIN
 * @since 2018-4-21
 */
@Data
@AllArgsConstructor
public class SimpleResponse {

    /**
     * 响应内容。
     */
    private Object content;
}
