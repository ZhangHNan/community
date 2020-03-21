package wanzhi.gulu.community.dto;

import lombok.Data;

//创建评论要获取的参数
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
