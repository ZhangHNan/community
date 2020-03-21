package wanzhi.gulu.community.dto;

import lombok.Data;
import wanzhi.gulu.community.model.User;

//评论的数据传输模型
@Data
public class CommentDTO {
    private Long id;
    private Long parentId; //被评论的问题id或者评论id
    private Integer type;  //1表示被评论的是问题，2表示被评论的是评论
    private Long commentator; //（评论人）谁评论？
    private String content;  //评论的内容
    private Long gmtCreate;  //创建时间
    private Long gmtModified; //修改时间
    private Long likeCount;  //点赞数
    private Long commentCount; //这条评论的二级评论数
    private User user;     //评论人详情
}
