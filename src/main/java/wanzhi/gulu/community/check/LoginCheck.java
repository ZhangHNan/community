package wanzhi.gulu.community.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 抽取登录状态检查的重复代码
 */
@Component
public class LoginCheck {

    @Autowired
    UserMapper userMapper;

    /**
     * 检查cookie中的token数据，根据token查询数据库中有无登录数据，并将user数据存入session
     * 没有登录检查的话，重启服务器后，再次访问页面是无法变成登录状态的！所以到所有页面都要有登录检查
     * @param cookies 用户获取token数据
     * @param request 用于获取session和将user数据存入session
     */
    public void check(Cookie[] cookies, HttpServletRequest request){
        if (cookies != null && cookies.length!=0){//用户关闭浏览器后cookie可能清空，cookies为null执行下面遍历就会出现空指针异常
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
    }
}
