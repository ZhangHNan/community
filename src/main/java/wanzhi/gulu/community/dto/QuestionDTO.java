package wanzhi.gulu.community.dto;

import lombok.Data;
import wanzhi.gulu.community.model.User;

//DTO类似信使，是同步系统中的Message。一个DTO包含的信息是完整的。
@Data
public class QuestionDTO {
    private Long id;
    private String title;       //问题标题
    private String description; //问题详情
    private Long gmtCreate;     //创建时间
    private Long gmtModified;   //修改时间
    private Long creator;       //创建人
    private Integer commentCount; //问题的一级评论数
    private Integer viewCount;    //浏览量
    private Integer likeCount;    //点赞数
    private String tag;         //标签
    private User user;          //创建人详情
}
