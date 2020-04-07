package com.hdw.common.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传配置
 *
 * @Author TuMingLong
 * @Date 2020-01-03 15:48
 */
@Configuration
public class MultipartConfig {

    //显示声明CommonsMultipartResolver为mutipartResolver
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(100 * 1024 * 1024);
        //上传文件大小 100M 5*1024*1024
        resolver.setMaxUploadSize(100 * 1024 * 1024);
        return resolver;
    }

}
