package wanzhi.gulu.community.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginCheck loginCheck;

    @Autowired
    QuestionService questionService;

    @GetMapping(value = {"/", "/index"})
    public String index(HttpServletRequest request,
                        @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                        @RequestParam(value = "rows",defaultValue = "5")Integer rows,
                        Model model) {

        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        Cookie[] cookies = request.getCookies();
        loginCheck.check(cookies,request);

        //获取页面帖子数据用于首页展示
        PageDTO pageDTO = questionService.findAll(currentPage,rows);
        model.addAttribute("pageDTO",pageDTO);

        return "index";
    }
}
