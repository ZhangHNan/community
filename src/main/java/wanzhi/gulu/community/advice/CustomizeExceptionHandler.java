package wanzhi.gulu.community.advice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import wanzhi.gulu.community.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义异常处理器
 *     要用@ControllerAdvice注解
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)//表示要处理的异常类型
    ModelAndView handleControllerException(HttpServletRequest request,
                                           Throwable ex,//Exception extends Throwable
                                           Model model){
        HttpStatus status = getStatus(request);
        if(ex instanceof CustomizeException){
            //如果是自定义异常，我是能处理的，就要异常消息获取即可
            model.addAttribute("message",ex.getMessage());
        }else{
            //如果不是自定义异常，我处理不了，就说服务器冒烟了
            model.addAttribute("message","服务器冒烟了，要不然你稍后再试试！？！");
        }
        //model是用于存数据到request域中的，ModelAndView是做视图解析的（页面跳转及显示）
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request){
        //获取异常状态码
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            //return 500 服务器异常
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
