# 权限组件

* imooc-security-core: 权限核心配置
* imooc-security-browser: 浏览器权限配置
* imooc-security-demo: 示例代码

## 使用方式

```xml
<dependency>
    <groupId>com.imooc.security</groupId>
    <artifactId>imooc-security-browser</artifactId>
    <version>${imooc.security.version}</version>
</dependency>
```

提供了图片验证码和短信验证码默认实现，通过实现接口可以提供自定义的配置。