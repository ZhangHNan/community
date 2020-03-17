package wanzhi.gulu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("你找的问题不在了，要不要换个试试？"),
    PERMISSION_DENIED("您没有这个权限！");//这里定义的枚举是为了给下面构造函数中传入message准备的

    private String message;

    @Override
    public String getMassage() {//获取枚举的message，将枚举字符串转为字符串
        return message;
    }

    CustomizeErrorCode(String message) {//使用枚举的时候，枚举中的字符串传入给了message
        this.message = message;
    }
}
