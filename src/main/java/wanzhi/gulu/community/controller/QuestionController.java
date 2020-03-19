package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wanzhi.gulu.community.dto.CommentDTO;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.service.CommentService;
import wanzhi.gulu.community.service.QuestionService;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.findQuestionById(id);
        List<CommentDTO> commentDTOs = commentService.listByQuestionId(id);
        //累计阅读数
        questionService.incView(id);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("commentDTOS",commentDTOs);
        return "question";
    }
}
