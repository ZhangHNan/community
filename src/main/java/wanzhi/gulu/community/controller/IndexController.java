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

    @GetMapping(value={"/","/index"})
    public String index(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                request.getSession().setAttribute("user",user);
                break;
            }
        }
        return "index";
    }
}
