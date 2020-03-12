package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.mapper.UserMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginCheck loginCheck;

    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request) {

        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        Cookie[] cookies = request.getCookies();
        loginCheck.check(cookies,request);
        return "index";
    }
}
