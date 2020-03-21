package wanzhi.gulu.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.mapper.NotificationMapper;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.service.NotificationService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录拦截器 ：作登录状态检查，以及获取未读通知
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    LoginCheck loginCheck;

    @Autowired
    NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        //若服务器重启，用户第一次访问页面的请求没有检查登录状态，则导航栏不会显示已登录状态
        Cookie[] cookies = request.getCookies();
        //登录检查
        loginCheck.check(cookies, request);
        //获取未读通知数
        User user = (User)request.getSession().getAttribute("user");
        if(user!=null){
            //如果登录才查询未读通知
            Integer unread = notificationService.findUnreadCountByReceiver(user.getId());
            request.getSession().setAttribute("unread",unread);
        }
        return true;
    }
}
