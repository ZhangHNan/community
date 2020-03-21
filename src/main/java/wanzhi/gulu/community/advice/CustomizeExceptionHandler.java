package wanzhi.gulu.community.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wanzhi.gulu.community.dto.CommentResultDTO;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义异常处理器 ：用于处理上下文异常,即请求进来了但代码中出的异常
 * 要用@ControllerAdvice注解
 */
@ControllerAdvice
public class CustomizeExceptionHandler {


    @ExceptionHandler(Exception.class)//表示要处理的异常类型
    ModelAndView handleControllerException(HttpServletRequest request,
                                           Throwable ex,//Exception extends Throwable
                                           Model model,
                                           HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //返回json
            CommentResultDTO resultDTO;
            if (ex instanceof CustomizeException) {
                //如果是自定义异常，我是能处理的，就要异常消息获取即可
                resultDTO = CommentResultDTO.errorOf((CustomizeException) ex);
            } else {
                //如果不是自定义异常，我处理不了，就说服务器冒烟了
                resultDTO = CommentResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //错误页面跳转
            if (ex instanceof CustomizeException) {
                //如果是自定义异常，我是能处理的，就要异常消息获取即可
                model.addAttribute("message", ex.getMessage());
            } else {
                //如果不是自定义异常，我处理不了，就说服务器冒烟了
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMassage());
            }
        }
        //model是用于存数据到request域中的，ModelAndView是做视图解析的（页面跳转及显示）
        return new ModelAndView("error");
    }

    //获取状态码的方法
    private HttpStatus getStatus(HttpServletRequest request) {
        //获取异常状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            //return 500 服务器异常
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
