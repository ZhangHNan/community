package wanzhi.gulu.community.dto;

import lombok.Data;
import wanzhi.gulu.community.model.User;

//通知的数据模型
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate; //创建时间
    private Long notifier; //创建人id
    private Long outerid; //具体的question的id
    private Integer status; //状态：已读、未读
    private User user; //创建人详情
    private Integer type; //类型：评论还是问题
    private String typeName; //类型描述，前端要直接显示 xxx 回复了评论/问题 xxx
    private String outerTitle; //哪条评论的content或问题的title
}
