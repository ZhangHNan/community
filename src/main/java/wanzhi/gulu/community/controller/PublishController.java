package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.Question;
import wanzhi.gulu.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginCheck loginCheck;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request){
        //进入publish时，也要获取cookies中的token数据，根据token查询数据库中有无登录数据
        Cookie[] cookies = request.getCookies();
        loginCheck.check(cookies,request);
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question,
                            HttpServletRequest request,//用于获取cookies
                            Model model//用于传递信息
    ){
        if (question.getTitle()==null||question.getTitle().equals("")){
            model.addAttribute("error","标题不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            return "publish";
        }
        if (question.getDescription()==null||question.getDescription().equals("")){
            model.addAttribute("error","内容不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            return "publish";
        }
        if (question.getTag()==null||question.getTag().equals("")){
            model.addAttribute("error","标签不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            return "publish";
        }
        User user=null; //下面要判断这个对象是不是null，此处不能用new User(),这样user就不是null了
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {//用户关闭浏览器后cookie可能清空，cookies为null执行下面遍历就会出现空指针异常
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user!=null){//防止出现用户浏览器未关，服务器就删除数据库的情况下还能正常运行（保证下面user.getId()能返回值）
                    question.setCreator(user.getId());
                    question.setGmtCreate(System.currentTimeMillis());
                    question.setGmtModified(question.getGmtCreate());
                    }
                    break;
                }
            }
        }
        if(user == null){
            model.addAttribute("error","用户未登录");
            //未登录时发帖页面的回调
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
//            System.out.println("未登录！");
            loginCheck.check(cookies,request);
//            System.out.println("question:"+question);
            return "publish";//要使用model传值就不能重定向
        }
        questionMapper.create(question);
//        System.out.println("question:"+question);
        return "redirect:/";
    }
}
