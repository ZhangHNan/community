package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    public List<QuestionDTO> findAll(){
        List<QuestionDTO> questionDTOs = questionMapper.findDTOAll();
        for(QuestionDTO questionDTO: questionDTOs){
            User user = userMapper.findById(questionDTO.getCreator());
            questionDTO.setUser(user);
            System.out.println("QuestionService的questionDTO--"+questionDTO.getId()+":"+questionDTO);
        }
//        System.out.println("QuestionService的所有QuestionDTO--："+questionDTOs);
        return questionDTOs;
    }
}
