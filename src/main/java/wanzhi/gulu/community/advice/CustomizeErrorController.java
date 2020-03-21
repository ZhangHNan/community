package wanzhi.gulu.community.advice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 如果访问不存在的地址，异常message是空的，默认将message设置为 No message available
 * 自定义ErrorController 自定义错误控制器 ：用于处理请求错误，请求还没有进来：4xx、5xx
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")//就是@RequestMapping("error")
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
                                  Model model) {
        System.out.println("CustomizeErrorController work …… !");
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {//如果是4xx错误
            model.addAttribute("message", "你这个请求错了吧？要不然换个姿势？");
        }
        if (status.is5xxServerError()) {//如果是5xx错误
            model.addAttribute("message", "服务器冒烟了，要不然你稍后再试试！？！");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
