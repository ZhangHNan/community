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
    private Integer notificationRows;//设置通知页每页展示数据行数

    @Value("${page.notification.buttonCount}")
    private Integer notificationButtonCount;//设置通知页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个

    //到通知页的分页查询
    public PageDTO findPage(Integer currentPage,Long id){
        return pageUtils.autoStructureNotificationPageDTO(currentPage, notificationRows,notificationButtonCount,id);
    }

    //通过接收人查询未读通知数
    public Integer findUnreadCountByReceiver(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus())
                .andReceiverEqualTo(id);
        return notificationMapper.countByExample(example);
    }
}
