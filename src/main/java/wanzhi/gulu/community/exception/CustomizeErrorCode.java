package wanzhi.gulu.community.exception;

/**
 * 自定义错误信息枚举对象 ：code是自定义错误代码，message是错误消息
 * 相当于设置自定义异常消息的选项（到异常页面）,可以用作与json数据直接返回（直接显示json数据，参照CommentResultDTO）
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(4001,"你找的问题不在了，要不要换个试试？"),//这个是页面请求异常，应该抛到异常页面
    PERMISSION_DENIED(4002,"您没有这个权限！"), //这个是页面请求异常，应该抛到异常页面
    TARGET_PARAM_ERROR(4004,"该问题或评论不见了，无法进行评论！"), //这个是Ajax请求异常，应该返回json数据（没parentId）
    LOGIN_NOT_FOUND(3005,"未登录，请先登录!"), //这个是Comment中的Ajax请求异常，应该返回json数据
    SYSTEM_ERROR(5000,"服务器冒烟了，要不然你稍后再试试！？！"),//未知异常，没有处理的，应该抛到异常页面
    TYPE_PARAM_ERROR(4005,"评论类型错误，或不存在！"),
    COMMENT_NOT_FOUND(4004,"评论不存在！"),
    CONTENT_IS_EMPTY(4007,"评论内容不能为空！"),
    FILE_UPLOAD_FAIL(7005,"图片上传失败！")
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
