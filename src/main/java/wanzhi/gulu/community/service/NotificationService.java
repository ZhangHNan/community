package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.enums.NotificationStatusEnum;
import wanzhi.gulu.community.mapper.NotificationMapper;
import wanzhi.gulu.community.model.NotificationExample;
import wanzhi.gulu.community.util.PageUtils;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    private PageUtils pageUtils;

    @Value("${page.notification.rows}")
    private String notificationRows;//设置通知页每页展示数据行数

    @Value("${page.notification.buttonCount}")
    private String notificationButtonCount;//设置通知页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个

    public PageDTO findPage(Integer currentPage,Long id){
        return pageUtils.autoStructureNotificationPageDTO(currentPage, Integer.parseInt(notificationRows), Integer.parseInt(notificationButtonCount),id);
    }

    public Integer findUnreadCountByReceiver(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        int unread = notificationMapper.countByExample(example);
        return unread;
    }
}
