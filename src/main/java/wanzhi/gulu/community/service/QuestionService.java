package wanzhi.gulu.community.service;

import org.apache.commons.lang3.StringUtils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private Integer indexRows;//设置首页每页展示数据行数

    @Value("${page.index.buttonCount}")
    private Integer indexButtonCount;//设置首页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个

    @Value("${page.question.rows}")
    private Integer questionRows;//设置我的问题页每页展示数据行数

    @Value("${page.question.buttonCount}")
    private Integer questionButtonCount;//设置我的问题页每页展示页面按钮数。请设置为奇数，设置为偶数中间段还是奇数个，头和尾才是偶数个


    //首页查询帖子并作分页
    public PageDTO findPage(Integer currentPage, String search) {
        //传入要跳转的页面、每页显示数据条数、每页显示指定到某页的按钮数即可自动构建pageDTO对象并返回
        return pageUtils.autoStructureQuestionPageDTO(currentPage, indexRows, indexButtonCount,search);
    }

    //我的问题页分页查询帖子
    public PageDTO findPageByCreator(Integer currentPage, Long id) {
        return pageUtils.autoStructureQuestionPageDTOByCreator(currentPage, questionRows, questionButtonCount, id);
    }

    //根据id查询QuestionDTO ：到问题详情页时
    public QuestionDTO findQuestionById(Long id) {
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


    //更新或创建帖子 ： 发布或编辑问题功能
    public void updateOrCreate(Question question) {
        if(question.getId()==null){
//            questionMapper.create(question);
            question.setGmtCreate(question.getGmtModified());
            questionMapper.insertSelective(question);
        }else {
//            questionMapper.update(question);
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, questionExample);
        }
    }

    //增加阅读数
    public void incView(Long id) {
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

    //查询相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regxpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regxpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
