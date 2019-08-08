package com.hdw;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;


/**
 * @author TuMinglong
 * @description Application
 * @date 2017年9月5日下午8:55:08
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableCaching
@EnableDubbo
public class UpmsServiceApplication extends SpringBootServletInitializer {

    protected final static Logger logger = LoggerFactory.getLogger(UpmsServiceApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        logger.info("----UpmsServiceApplication 启动----");
        return application.sources(UpmsServiceApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(UpmsServiceApplication.class, args);
        logger.info("----UpmsServiceApplication 启动----");
    }

}
