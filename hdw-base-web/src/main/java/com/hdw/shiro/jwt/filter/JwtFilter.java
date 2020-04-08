package com.hdw.shiro.jwt.filter;

import com.hdw.common.api.CommonResult;
import com.hdw.common.constant.CommonConstant;
import com.hdw.common.util.JacksonUtil;
import com.hdw.shiro.jwt.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description 鉴权登录拦截器
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 * @Author TuMingLong
 * @Date 2020/4/5 13:41
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            executeLogin(request,response);
            return true;
        }catch (Exception e){
            throw new AuthorizationException("Token失效，请重新登录", e);
        }
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        //TODO: 拦截请求头中是否含有token
        String headerToken = httpServletRequest.getHeader(CommonConstant.JWT_DEFAULT_TOKEN_NAME);
        //TODO: 拦截请求参数中是否含有token
        String parameterToken=httpServletRequest.getParameter(CommonConstant.JWT_DEFAULT_TOKEN_NAME);
        String token=null;
        //TODO:当请求头中不含token，请求参数中含有token时，通过。用于文件上传及接口
        if(StringUtils.isEmpty(headerToken)){
            token=parameterToken;
        }else {
            token=headerToken;
        }
        JwtToken jwtToken=new JwtToken(token);
        //TODO: 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request,response).login(jwtToken);
        //TODO:  如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 判断用户是否想要登入
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(CommonConstant.JWT_DEFAULT_TOKEN_NAME);
        return authorization != null;
    }


    /**
     * 对跨域提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        //TODO: 拦截请求头中是否含有token
        String headerToken = httpServletRequest.getHeader(CommonConstant.JWT_DEFAULT_TOKEN_NAME);
        //TODO: 拦截请求参数中是否含有token
        String parameterToken=httpServletRequest.getParameter(CommonConstant.JWT_DEFAULT_TOKEN_NAME);
        if(StringUtils.isEmpty(headerToken) && StringUtils.isEmpty(parameterToken)){
            String url = httpServletRequest.getRequestURI();
            log.error("JwtFilter preHandle url：{}", url);
            responseError(request, response);
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 非法url返回身份错误信息
     */
    private void responseError(ServletRequest request, ServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            response.setContentType("application/json; charset=utf-8");
            out.print(JacksonUtil.toJson(CommonResult.unauthorized("")));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
