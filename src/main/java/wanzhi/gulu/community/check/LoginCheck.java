package wanzhi.gulu.community.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 抽取重复代码：检查cookie中的token数据，根据token查询数据库中有无登录数据
 */
@Component
public class LoginCheck {

    @Autowired
    UserMapper userMapper;

    public void check(Cookie[] cookies, HttpServletRequest request){
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
    }
}
