package wanzhi.gulu.community.exception;

/**
 * 相当于设置自定义异常消息的选项
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不要换个试试？"),
    PERMISSION_DENIED(2002,"您没有这个权限！"),
    TARGET_PARAM_NOT_FOUND(2004,"该问题或评论不见了，无法进行评论！"),
    LOGIN_NOT_FOUND(2005,"未登录，请先登录!"),
    ;//这里定义的枚举是为了给下面构造函数中传入message准备的

    private String message;
    private Integer code;

    @Override
    public String getMassage() {//获取枚举的message，将枚举字符串转为字符串
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code,String message) {//使用枚举的时候，枚举中的字符串传入给了message
        this.code = code;
        this.message = message;
    }
}
