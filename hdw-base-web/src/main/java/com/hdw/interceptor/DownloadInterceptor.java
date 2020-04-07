package com.hdw.interceptor;

import com.hdw.common.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 下载拦截器
 * @Author TuMingLong
 * @Date 2019/11/1 14:54
 */
@Slf4j
@Component
public class DownloadInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private InterceptorProperties interceptorProperties;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果访问的不是控制器,则跳出,继续执行下一个拦截器
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 访问路径
        String url = request.getRequestURI();
        // 访问全路径
        String fullUrl = request.getRequestURL().toString();

        // 未启用资源访问时，返回错误消息
        if (!interceptorProperties.getDownload().isEnabled()) {
            log.error("下载已关闭，非法下载：{}", fullUrl);
            new GlobalException("非法下载");
            return false;
        }

        // 下载拦截器，业务处理代码
        log.info("DownloadInterceptor...");

        // 访问token，如果需要，可以设置参数，进行鉴权
        String token = request.getParameter("token");

        log.info("url:{}", url);
        log.info("fullUrl:{}", fullUrl);
        log.info("token:{}", token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 记录实际下载日志...
    }
}
