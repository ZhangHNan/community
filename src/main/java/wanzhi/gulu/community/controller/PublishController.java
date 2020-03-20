package wanzhi.gulu.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import wanzhi.gulu.community.cache.TagCache;
import wanzhi.gulu.community.check.LoginCheck;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.dto.TagDTO;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.Question;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.model.UserExample;
import wanzhi.gulu.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginCheck loginCheck;

    //到发布页面
    @GetMapping("/publish")
    public String publish(HttpServletRequest request,Model model){
        //进入publish时，也要获取cookies中的token数据，根据token查询数据库中有无登录数据
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    //到编辑页面（同发布页面）
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") long id,
                       HttpServletRequest request,
                       Model model){
        //判断执行这个请求的是否是本人（登录的人）
        User sessionUser = (User)request.getSession().getAttribute("user");
//        Integer creator = questionMapper.findCreatorById(id);
        Question question1 = questionMapper.selectByPrimaryKey(id);
        Long creator = question1.getCreator();

        if(!sessionUser.getId().equals(creator)){
            //不是本人操作直接抛自定义异常
            throw new CustomizeException(CustomizeErrorCode.PERMISSION_DENIED);
            //如果不是本人操作直接返回首页
//            return "redirect:/index";
        }
        //安全校验通过，查询信息并返回
//        QuestionDTO question = questionMapper.findById(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        //用于页面回显
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",id);
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    //发布功能 或者 编辑完成，重新发布
    @PostMapping("/publish")
    public String doEdit(Question question,
                         HttpServletRequest request,//用于获取cookies
                         Model model//用于传递信息
    ){
        if(question.getId()!=null){//如果是修改操作，需先进行登录校验
            //判断执行这个请求的是否是本人（登录的人）
            User sessionUser = (User)request.getSession().getAttribute("user");
//            Integer creator = questionMapper.findCreatorById(question.getId());
            Question question1 = questionMapper.selectByPrimaryKey(question.getId());
            Long creator = question1.getCreator();
            if(!sessionUser.getId().equals(creator)){
                //不是本人操作直接抛自定义异常
                throw new CustomizeException(CustomizeErrorCode.PERMISSION_DENIED);
                //如果不是本人操作直接返回首页
//                return "redirect:/index";
            }
        }

        //安全校验通过，执行修改逻辑
        if (question.getTitle()==null||question.getTitle().equals("")){
            model.addAttribute("error","标题不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            model.addAttribute("id",question.getId());
            model.addAttribute("tags",TagCache.get());
            return "publish";
        }
        if (question.getDescription()==null||question.getDescription().equals("")){
            model.addAttribute("error","内容不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            model.addAttribute("id",question.getId());
            model.addAttribute("tags",TagCache.get());
            return "publish";
        }
        if (question.getTag()==null||question.getTag().equals("")){
            model.addAttribute("error","标签不能为空");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            model.addAttribute("id",question.getId());
            model.addAttribute("tags",TagCache.get());
            return "publish";
        }
        String invalid = TagCache.filterInvalid(question.getTag());
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","有非法标签请改正！");
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
            model.addAttribute("id",question.getId());
            model.addAttribute("tags",TagCache.get());
            return "publish";
        }
        User user=null; //下面要判断这个对象是不是null，此处不能用new User(),这样user就不是null了
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {//用户关闭浏览器后cookie可能清空，cookies为null执行下面遍历就会出现空指针异常
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
//                    user = userMapper.findByToken(token);
                    //token查User
                    UserExample userExample = new UserExample();
                    //使用这种方法拼接sql
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size()!=0){//防止出现用户浏览器未关，服务器就删除数据库的情况下还能正常运行（保证下面user.getId()能返回值）
                        user = users.get(0);
                        question.setId(question.getId());
                        question.setCreator(user.getId());
                        question.setGmtModified(System.currentTimeMillis());
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
            model.addAttribute("questionId",question.getId());
            model.addAttribute("tags",TagCache.get());
//            System.out.println("未登录！");
            loginCheck.check(cookies,request);
//            System.out.println("question:"+question);
            return "publish";//要使用model传值就不能重定向
        }
        questionService.updateOrCreate(question);
//        System.out.println("question:"+question);
        return "redirect:/";
    }
}
