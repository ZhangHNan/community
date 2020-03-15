package wanzhi.gulu.community.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理分页的组件
 */
@Component
public class PageUtils {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    //PageDTO的自动构造方法，传入PageDTO对象即可构造pageDTO并返回
    public PageDTO autoStructurePageDTO(int currentPage, int rows, int buttonCount) {
        return autoStructurePageDTOByCreator(currentPage, rows, buttonCount, null);
    }

    public PageDTO autoStructurePageDTOByCreator(int currentPage, int rows, int buttonCount, Integer id) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setCurrentPage(currentPage);
        pageDTO.setRows(rows);
        //给PageDTO赋值
        //查询帖子（数据）总数并赋值
        Integer totalCount = 0;
        if (id == null) {
            //查询所有帖子总数
            totalCount = questionMapper.findTotalCount();
        } else {
            //根据创建者来查询帖子总数
            totalCount = questionMapper.findTotalCountById(id);
        }
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

        //分页查询帖子（需要传入开始索引和显示行数）
        List<QuestionDTO> questionDTOs = null;
        if (id == null) {
            //分页查询所有帖子
            questionDTOs = questionMapper.findByPage(start, rows);
        } else {
            //根据创建者来分页查询帖子
            questionDTOs = questionMapper.findByPageByCreator(start, rows, id);
        }

        for (QuestionDTO questionDTO : questionDTOs) {
            User user = userMapper.findById(questionDTO.getCreator());
            questionDTO.setUser(user);
        }
        //将查询出来的帖子赋给PageDTO
        pageDTO.setQuestionDTOS(questionDTOs);

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
        return pageDTO;
    }
}
