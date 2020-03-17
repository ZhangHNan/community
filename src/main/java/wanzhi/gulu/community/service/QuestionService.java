package wanzhi.gulu.community.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.dto.PageDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;
import wanzhi.gulu.community.mapper.QuestionExtMapper;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.Question;
import wanzhi.gulu.community.model.QuestionExample;
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
    QuestionExtMapper questionExtMapper;

    @Autowired
    PageUtils pageUtils;

    @Value("${page.index.rows}")
    private String indexRows;//设置首页每页展示数据行数

    @Value("${page.index.buttonCount}")
    private String indexButtonCount;//设置首页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个

    @Value("${page.question.rows}")
    private String questionRows;//设置我的问题页每页展示数据行数

    @Value("${page.question.buttonCount}")
    private String questionButtonCount;//设置我的问题页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个


    //查询帖子并作分页
    public PageDTO findPage(Integer currentPage) {
        //传入要跳转的页面、每页显示数据条数、每页显示指定到某页的按钮数即可自动构建pageDTO对象并返回
        return pageUtils.autoStructurePageDTO(currentPage, Integer.parseInt(indexRows), Integer.parseInt(indexButtonCount));
    }

    public PageDTO findPageByCreator(Integer currentPage, Integer id) {
        return pageUtils.autoStructurePageDTOByCreator(currentPage, Integer.parseInt(questionRows), Integer.parseInt(questionButtonCount), id);
    }

    public QuestionDTO findQuestionById(Integer id) {
//        QuestionDTO questionDTO = questionMapper.findById(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){//如果查询一个没有的帖子，抛一个自定义的异常
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
//        User user = userMapper.findById(questionDTO.getCreator());
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }


    //更新或创建帖子
    public void updateOrCreate(Question question) {
        if(question.getId()==null){
//            questionMapper.create(question);
            question.setGmtCreate(question.getGmtModified());
            questionMapper.insert(question);
        }else {
//            questionMapper.update(question);
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, questionExample);
        }
    }

    public void incView(Integer id) {
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount()+1);
//        QuestionExample example = new QuestionExample();
//        example.createCriteria()
//                .andIdEqualTo(id);
//        questionMapper.updateByExampleSelective(updateQuestion, example);
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incView(updateQuestion);
    }
}
