package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import wanzhi.gulu.community.dto.CommentDTO;
import wanzhi.gulu.community.mapper.CommentMapper;
import wanzhi.gulu.community.model.Comment;
import wanzhi.gulu.community.model.User;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    CommentMapper commentMapper;

    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());//注意这个ParentId可能不存在
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(comment.getGmtModified());
        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            comment.setCommentator(user.getId());
        }else{
            comment.setCommentator(999);
        }
        comment.setLikeCount(0L);
        commentMapper.insert(comment);
        return null;
    }
}
