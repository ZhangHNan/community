package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wanzhi.gulu.community.dto.CommentDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.enums.CommentTypeEnum;
import wanzhi.gulu.community.service.CommentService;
import wanzhi.gulu.community.service.QuestionService;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    //到问题详情页面
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model){
        //累计阅读数
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.findQuestionById(id);
        List<CommentDTO> commentDTOs = commentService.listByTargetId(id,CommentTypeEnum.QUESTION);
        List<QuestionDTO> relatedQuestionDTOS = questionService.selectRelated(questionDTO);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("commentDTOS",commentDTOs);
        model.addAttribute("relatedQuestionDTOS",relatedQuestionDTOS);
        return "question";
    }
}
