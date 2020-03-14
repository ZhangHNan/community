package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${page.rows}")
    private String rows;//设置每页展示数据行数

    @Value("${page.buttonCount}")
    private String buttonCount;//设置每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个

    //查询帖子并作分页
    public PageDTO findPage(Integer currentPage){
        //传入要跳转的页面、每页显示数据条数、每页显示指定到某页的按钮数即可自动构建pageDTO对象并返回
        return pageUtils.autoStructurePageDTO(currentPage, Integer.parseInt(rows),Integer.parseInt(buttonCount));
    }
}
