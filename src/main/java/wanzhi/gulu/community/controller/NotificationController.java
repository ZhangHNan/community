package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import wanzhi.gulu.community.enums.NotificationStatusEnum;
import wanzhi.gulu.community.enums.NotificationTypeEnum;
import wanzhi.gulu.community.mapper.CommentMapper;
import wanzhi.gulu.community.mapper.NotificationMapper;
import wanzhi.gulu.community.model.Comment;
import wanzhi.gulu.community.model.CommentExample;
import wanzhi.gulu.community.model.Notification;
import wanzhi.gulu.community.model.NotificationExample;

import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    CommentMapper commentMapper;

    //已读通知操作：改变通知状态，同时跳转到相应的question页面
    @GetMapping("/read")
    public String read(@RequestParam("id") Long id,
                       @RequestParam("type") Integer type,
                       @RequestParam("outerid")Long outerid){
        if (type==NotificationTypeEnum.REPLY_QUESTION.getType()){
            //标为已读
            Notification notification = notificationMapper.selectByPrimaryKey(id);
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            NotificationExample example = new NotificationExample();
            example.createCriteria()
                    .andIdEqualTo(id);
            notificationMapper.updateByExample(notification, example);
            return "forward:/question/"+outerid;
        }else{
            //标为已读
            Notification notification = notificationMapper.selectByPrimaryKey(id);
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            NotificationExample example = new NotificationExample();
            example.createCriteria()
                    .andIdEqualTo(id);
            notificationMapper.updateByExample(notification, example);
            //查询outerid对应的questionId
            Comment comment = commentMapper.selectByPrimaryKey(outerid);
            Long parentId = comment.getParentId();
            //跳转
            return "forward:/question/"+parentId;
        }
    }
}
