package wanzhi.gulu.community.exception;

/**
 * 自定义异常
 * 为什么要继承RuntimeException?
 * 不需要try catch就可以throw
 */
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(CustomizeErrorCode customizeErrorCode){//使用这个自定义异常需要传入自定的异常message
        this.message=customizeErrorCode.getMassage();
    }

    public String getMessage(){
        return message;
    }
}
