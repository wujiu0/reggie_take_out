package org.example.reggie.filter;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.BaseContext;
import org.example.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// TODO
//  使用拦截器interceptor重写
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestURI = httpServletRequest.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };


//        判断本次请求是否需要处理
        if (check(urls, requestURI)) {
            chain.doFilter(request, response);
            return;
        }

//        判断登录状态，若已登录则放行--后台
        if (httpServletRequest.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，id为{}", (httpServletRequest.getSession().getAttribute("employee")));

            Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            chain.doFilter(request, response);
            return;
        }


//        判断登录状态，若已登录则放行--客户端
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            log.info("用户已登录，id为{}", (httpServletRequest.getSession().getAttribute("user")));

            Long userId = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            chain.doFilter(request, response);
            return;
        }

        log.info("未通过");
        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = ANT_PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

}
