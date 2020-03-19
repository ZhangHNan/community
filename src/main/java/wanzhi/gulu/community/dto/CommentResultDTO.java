package wanzhi.gulu.community.dto;

import lombok.Data;
import org.springframework.web.servlet.ModelAndView;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;

/**
 * 用于封装CommentController中请求postAPI返回的json数据
 */
@Data
public class CommentResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    //请求错误的话返回的数据
    public static CommentResultDTO errorOf(Integer code,String message){
        CommentResultDTO resultDTO = new CommentResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static CommentResultDTO errorOf(CustomizeErrorCode errorCode){
        return errorOf(errorCode.getCode(),errorCode.getMassage());
    }

    //请求成功的话返回的数据
    public static CommentResultDTO okOf(){
        CommentResultDTO resultDTO = new CommentResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> CommentResultDTO okOf(T t){
        CommentResultDTO resultDTO = new CommentResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

    //作json格式返回异常时需要用
    public static CommentResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }
}
