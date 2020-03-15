package wanzhi.gulu.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wanzhi.gulu.community.check.LoginCheck;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    LoginCheck loginCheck;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        //若服务器重启，用户第一次访问页面的请求没有检查登录状态，则导航栏不会显示已登录状态
        Cookie[] cookies = request.getCookies();
        loginCheck.check(cookies, request);
        return true;
    }
}
