package wanzhi.gulu.community.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wanzhi.gulu.community.dto.NotificationDTO;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.dto.SearchDTO;
import wanzhi.gulu.community.enums.NotificationTypeEnum;
import wanzhi.gulu.community.mapper.*;
import wanzhi.gulu.community.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用的处理分页的组件
 */
@Component
public class PageUtils {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    CommentMapper commentMapper;

    //编写自动构建PageDTO的步骤
    private PageDTO autoStructurePageDTO(int currentPage, int rows, int buttonCount) {
        //查询数据总数TotalCount
        //构建分页模型
        //补充数据
        return null;
    }

    //QuestionPageDTO的自动构造方法，传入需要跳转页面、每页展示数据条数、每页显示按钮数即可构造pageDTO并返回
    public PageDTO autoStructureQuestionPageDTO(int currentPage, int rows, int buttonCount,String search) {
        PageDTO pageDTO;
        if (StringUtils.isBlank(search)){
            //没有搜索的时候
            pageDTO = autoStructureQuestionPageDTOByCreator(currentPage, rows, buttonCount, null);
        }else{
            //搜索
            pageDTO = autoStructureQuestionPageDTOBySearch(currentPage, rows, buttonCount, search);
        }
        return pageDTO;
    }

    private PageDTO autoStructureQuestionPageDTOBySearch(int currentPage, int rows, int buttonCount, String search) {
        PageDTO pageDTO;
        //查询总数TotalCount
        String[] searchs = StringUtils.split(search, " ");
        String regxpSearch = Arrays.stream(searchs).collect(Collectors.joining("|"));
        Integer totalCount = selectQuestionDTOTotalCountBySearch(regxpSearch);
        //构建分页模型
        pageDTO = autoStructurePageModel(currentPage, rows, buttonCount, totalCount);
        //补充分页数据
        pageDTO = injectQuestionDTODataSBySearch(pageDTO, regxpSearch);
        return pageDTO;
    }

    private Integer selectQuestionDTOTotalCountBySearch(String regxpSearch) {
        //根据创建者来查询帖子总数
        Integer totalCount = questionExtMapper.countBySearch(regxpSearch);
        return totalCount;
    }

    private PageDTO injectQuestionDTODataSBySearch(PageDTO pageDTO, String regxpSearch) {
        //分页查询帖子（需要传入开始索引和显示行数）
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setRegxpSearch(regxpSearch);
        searchDTO.setStart(pageDTO.getStart());
        searchDTO.setRows(pageDTO.getRows());
        List<Question> questions = questionExtMapper.selectBySearchPage(searchDTO);
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTOs.add(questionDTO);
        }

        for (QuestionDTO questionDTO : questionDTOs) {
//            User user = userMapper.findById(questionDTO.getCreator());
            User user = userMapper.selectByPrimaryKey(questionDTO.getCreator());
            questionDTO.setUser(user);
        }
        //将查询出来的帖子赋给PageDTO
        pageDTO.setDataS(questionDTOs);
        return pageDTO;
    }

    //QuestionPageDTO的自动构造方法：分三步：查总数、键模型、查数据
    public PageDTO autoStructureQuestionPageDTOByCreator(int currentPage, int rows, int buttonCount, Long id) {
        //查询总数TotalCount
        Integer totalCount = selectQuestionDTOTotalCount(id);
        //构建分页模型
        PageDTO pageDTO = autoStructurePageModel(currentPage, rows, buttonCount, totalCount);
        //补充分页数据
        pageDTO = injectQuestionDTODataS(pageDTO, id);
        return pageDTO;
    }

    //自动构建分页模型，但无数据
    private PageDTO autoStructurePageModel(int currentPage, int rows, int buttonCount, Integer totalCount) {
        PageDTO pageDTO = new PageDTO();
        //给PageDTO赋值
        pageDTO.setCurrentPage(currentPage);
        pageDTO.setRows(rows);
        //赋值数据总数
        pageDTO.setTotalCount(totalCount);

        //计算最后一页数（需要计算出帖子总数）并赋值
        int totalPage = countTotalPage(totalCount, rows);
        pageDTO.setTotalPage(totalPage);

        //计算开始索引并赋值
        int start = countStart(currentPage, rows);
        pageDTO.setStart(start);

        //计算展示按钮的值并赋值
        List<Integer> showButtons = countButton(currentPage, totalPage, buttonCount);
        pageDTO.setShowButtons(showButtons);

        //判断是否展示前面两个和后面两个按钮
        pageDTO = judgeShow(pageDTO);

        return pageDTO;
    }

    /**
     * @param totalCount 数据总量
     * @param rows       每页显示数据条数
     * @return 可显示的总的页数
     */
    private int countTotalPage(int totalCount, int rows) {
        return (totalCount % rows == 0) ? (totalCount / rows) : (totalCount / rows + 1);
    }

    //返回供数据库查询的开始索引
    private int countStart(Integer currentPage, Integer rows) {
        return (currentPage - 1) * rows;
    }

    /**
     * 页面跳转的按钮有：到首页、到上一页、到指定某一页（包含当前页）、到下一页、到尾页
     *
     * @param currentPage 当前页
     * @param totalPage   总页数（尾页）
     * @param buttonCount 指定要显示的按钮个数
     * @return 跳转指定某一页页面的按钮集合
     */
    private List<Integer> countButton(int currentPage, int totalPage, int buttonCount) {
        List<Integer> showButton = new ArrayList<>();
        //当前页的按钮为了美观要显示在所有按钮集合的中间，所以指定显示的按钮数要显示为奇数
        //而当当前页在前端（显示按钮数的一半）或后端时，当前按钮无法在中间显示，所以要固定显示。如：显示5个按钮，当前页在1和2的时候都是显示 1、2、3、4、5
        //当总页数小于指定要显示的按钮时
        if (totalPage <= buttonCount) {
            for (int i = 1; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在前端时
        if (currentPage < buttonCount / 2 + 1) {
            for (int i = 1; i <= buttonCount; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在后端时
        if (totalPage - currentPage < buttonCount / 2 + 1) {
            for (int i = totalPage - buttonCount + 1; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在中间时
        for (int i = currentPage - buttonCount / 2; i <= currentPage + buttonCount / 2; i++) {
            showButton.add(i);
        }
        return showButton;
    }

    /**
     * 判断到首页、到上一页、到下一页、到尾页 这四个按钮的显示情况
     *
     * @param pageDTO 将pageDTO对象传入，前提是currentPage、totalPage、showButtons要有值
     * @return 到首页、到上一页、到下一页、到尾页 这四个按钮的显示情况
     */
    private PageDTO judgeShow(PageDTO pageDTO) {
        //先默认4个都显示
        pageDTO.setShowFirst(true);
        pageDTO.setShowEnd(true);
        pageDTO.setShowLast(true);
        pageDTO.setShowNext(true);
        //如果当前页是1的话，不显示到上一页
        if (pageDTO.getCurrentPage() == 1) {
            pageDTO.setShowLast(false);
        }
        //如果当前页是最后一页的话，不显示到下一页
        if (pageDTO.getCurrentPage().equals(pageDTO.getTotalPage())) {
            pageDTO.setShowNext(false);
        }
        //如果展示按钮中包含1的话，不展示到首页
        List<Integer> showButtons = pageDTO.getShowButtons();
        for (int i : showButtons) {
            if (i == 1) {
                pageDTO.setShowFirst(false);
                break;
            }
        }
        //如果展示按钮中包含最后一页的话，不显示到尾页
        for (int i : showButtons) {
            if (i == pageDTO.getTotalPage()) {
                pageDTO.setShowEnd(false);
                break;
            }
        }
        //当页面一个数据页没有的时候都不显示
        if (pageDTO.getTotalCount() == 0) {
            pageDTO.setShowFirst(false);
            pageDTO.setShowEnd(false);
            pageDTO.setShowLast(false);
            pageDTO.setShowNext(false);
        }
        return pageDTO;
    }

    //QuestionPageDTO查询总数的方法
    private Integer selectQuestionDTOTotalCount(Long id) {
        Integer totalCount = 0;
        if (id == null) {
            //查询所有帖子总数
//            totalCount = questionMapper.findTotalCount();
            totalCount = questionMapper.countByExample(new QuestionExample());
        } else {
            //根据创建者来查询帖子总数
//            totalCount = questionMapper.findTotalCountById(id);
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(id);
            totalCount = questionMapper.countByExample(example);
        }
        return totalCount;
    }

    //QuestionPageDTO查询数据数的方法
    private PageDTO injectQuestionDTODataS(PageDTO<QuestionDTO> pageDTO, Long id) {
        //分页查询帖子（需要传入开始索引和显示行数）
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        if (id == null) {
            //分页查询所有帖子
//            questionDTOs = questionMapper.findByPage(start, rows);
            QuestionExample example = new QuestionExample();
            example.setOrderByClause("gmt_create desc");
            questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(pageDTO.getStart(), pageDTO.getRows()));
        } else {
            //根据创建者来分页查询帖子
//            questionDTOs = questionMapper.findByPageByCreator(start, rows, id);
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(id);
            example.setOrderByClause("gmt_create desc");
            questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(pageDTO.getStart(), pageDTO.getRows()));
        }
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTOs.add(questionDTO);
        }

        for (QuestionDTO questionDTO : questionDTOs) {
//            User user = userMapper.findById(questionDTO.getCreator());
            User user = userMapper.selectByPrimaryKey(questionDTO.getCreator());
            questionDTO.setUser(user);
        }
        //将查询出来的帖子赋给PageDTO
        pageDTO.setDataS(questionDTOs);
        return pageDTO;
    }

    public PageDTO autoStructureNotificationPageDTO(int currentPage, int rows, int buttonCount, Long receiver) {
        //查询总数TotalCount
        Integer totalCount = selectNotificationDTOTotalCountByReceiver(receiver);
        //构建分页模型
        PageDTO pageDTO = autoStructurePageModel(currentPage, rows, buttonCount, totalCount);
        //补充分页数据
        pageDTO = injectNotificationDTODataS(pageDTO,receiver);
        return pageDTO;
    }

    private Integer selectNotificationDTOTotalCountByReceiver(Long receiver) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(receiver);
        Integer totalCount = notificationMapper.countByExample(example);
        return totalCount;
    }

    private PageDTO injectNotificationDTODataS(PageDTO pageDTO,Long receiver) {
        //分页查询帖子（需要传入开始索引和显示行数）
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        List<Notification> notifications;
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(receiver);
        example.setOrderByClause("gmt_create desc");
        notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(pageDTO.getStart(), pageDTO.getRows()));

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTOs.add(notificationDTO);
        }
        for (NotificationDTO notificationDTO : notificationDTOs) {
//            User user = userMapper.findById(questionDTO.getCreator());
            User user = userMapper.selectByPrimaryKey(notificationDTO.getNotifier());
            notificationDTO.setUser(user);
            if (notificationDTO.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
                Question question = questionMapper.selectByPrimaryKey(notificationDTO.getOuterid());
                notificationDTO.setOuterTitle(question.getTitle());
                notificationDTO.setTypeName(NotificationTypeEnum.REPLY_QUESTION.getName());
            } else {
                Comment comment = commentMapper.selectByPrimaryKey(notificationDTO.getOuterid());
                notificationDTO.setOuterTitle(comment.getContent());
                notificationDTO.setTypeName(NotificationTypeEnum.REPLY_COMMENT.getName());
            }
        }
        //将查询出来的帖子赋给PageDTO
        pageDTO.setDataS(notificationDTOs);
        return pageDTO;
    }
}
