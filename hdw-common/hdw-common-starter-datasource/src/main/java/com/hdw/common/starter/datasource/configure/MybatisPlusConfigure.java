package com.hdw.common.starter.datasource.configure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description MybatisPlus配置类
 * @Author JacksonTu
 * @Date 2018/11/13 20:18
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfigure {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
