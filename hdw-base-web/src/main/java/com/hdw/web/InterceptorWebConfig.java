package com.hdw.web;

import com.hdw.interceptor.DownloadInterceptor;
import com.hdw.interceptor.InterceptorProperties;
import com.hdw.interceptor.UploadInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description WebMVC 拦截器配置
 * @Author TuMingLong
 * @Date 2020/4/8 19:00
 */
@Slf4j
@Configuration
public class InterceptorWebConfig implements WebMvcConfigurer {
    @Resource
    private InterceptorProperties interceptorProperties;

    @Resource
    private UploadInterceptor uploadInterceptor;

    @Resource
    private DownloadInterceptor downloadInterceptor;

    public InterceptorWebConfig() {
        log.debug("-----InterceptorWebConfig init-----");
    }

    /**
     * 注册自定义拦截器，添加拦截路径和排除拦截路径
     * 添加文件上传类型拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 上传拦截器
        if (interceptorProperties.getUpload().isEnabled()) {
            registry.addInterceptor(uploadInterceptor)
                    .addPathPatterns(interceptorProperties.getUpload().getIncludePaths());
        }

        // 下载拦截器注册
        if (interceptorProperties.getDownload().isEnabled()) {
            registry.addInterceptor(downloadInterceptor)
                    .addPathPatterns(interceptorProperties.getDownload().getIncludePaths());
        }
    }
}
