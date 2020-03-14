package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    LoginCheck loginCheck;

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          Model model,
                          @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                          HttpServletRequest request){

        //进入首页时，获取cookies中的token数据，根据token查询数据库中有无登录数据
        Cookie[] cookies = request.getCookies();
        User user = loginCheck.check(cookies, request);
        if (user == null){
            return "redirect:index";
        }

        if ("questions".equals(action)){//将字符写在前面，调用字符串的equals方法可以防止空指针异常。（action可能为null）
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PageDTO pageDTO = questionService.findPageByCreator(currentPage,user.getId());
        model.addAttribute("pageDTO",pageDTO);

        return "profile";
    }
}
