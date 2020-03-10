package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request) {

        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {//用户关闭浏览器后cookie可能清空，cookies为null执行下面遍历就会出现空指针异常
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        return "index";
    }
}
