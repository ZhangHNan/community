package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.util.PageUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    PageUtils pageUtils;

    //查询帖子并作分页
    public PageDTO findAll(Integer currentPage){
        PageDTO pageDTO = new PageDTO();
        pageDTO.setCurrentPage(currentPage);
        //设置每页展示数据行数
        int rows = 7;
        //设置每页展示页面按钮数
        int buttonCount =5; //请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个
        pageDTO.setRows(rows);
        //给PageDTO赋值
        //查询帖子总数并赋值
        int totalCount = questionMapper.findTotalCount();
        pageDTO.setTotalCount(totalCount);

        //计算最后一页数（需要计算出帖子总数）并赋值
        int totalPage = pageUtils.countTotalPage(totalCount,rows);
        pageDTO.setTotalPage(totalPage);

        //计算开始索引并赋值
        int start =pageUtils.countStart(currentPage,rows);
        pageDTO.setStart(start);

        //计算展示按钮的值并赋值
        List<Integer> showButtons= pageUtils.countButton(currentPage,totalPage,buttonCount);
//        System.out.println(showButtons);
        pageDTO.setShowButtons(showButtons);

        //判断是否展示前面两个和后面两个按钮
        pageDTO =pageUtils.judgeShow(pageDTO);

        //分页查询帖子（需要传入开始索引和显示行数）
        List<QuestionDTO> questionDTOs = questionMapper.findByPage(start,rows);
        for(QuestionDTO questionDTO: questionDTOs){
            User user = userMapper.findById(questionDTO.getCreator());
            questionDTO.setUser(user);
//            System.out.println("QuestionService的questionDTO--"+questionDTO.getId()+":"+questionDTO);
        }
//        System.out.println("QuestionService的所有QuestionDTO--："+questionDTOs);

        //将查询出来的帖子赋给PageDTO
        pageDTO.setQuestionDTOS(questionDTOs);
        System.out.println("pageDTO:"+pageDTO);

        return pageDTO;
    }
}
