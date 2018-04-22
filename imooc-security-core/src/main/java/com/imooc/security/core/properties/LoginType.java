package com.imooc.security.core.properties;

/**
 * 登录类型。
 *
 * @author DENGBIN
 * @since 2018-4-11
 */
public enum LoginType {

    /**
     * 1. 当登录成功时，将浏览器请求重新定向至触发登录请求的页面。<br/>
     * 2. 当登录失败时，将浏览器重定向至登录页面。
     */
    REDIRECT,

    /**
     * 登录后，返回登录成功或失败的信息，使用JSON格式。
     */
    JSON
}
