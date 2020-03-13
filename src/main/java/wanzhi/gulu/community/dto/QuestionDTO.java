package wanzhi.gulu.community.dto;

import lombok.Data;
import wanzhi.gulu.community.model.User;

//DTO类似信使，是同步系统中的Message。一个DTO包含的信息是完整的。
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
