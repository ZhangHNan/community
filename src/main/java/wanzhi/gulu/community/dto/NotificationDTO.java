package wanzhi.gulu.community.dto;

import lombok.Data;
import wanzhi.gulu.community.model.User;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User user;
    private String outerTile;
    private String type;
}
