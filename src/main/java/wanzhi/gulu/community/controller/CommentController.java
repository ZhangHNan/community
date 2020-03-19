package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import wanzhi.gulu.community.dto.CommentCreateDTO;
import wanzhi.gulu.community.dto.CommentResultDTO;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.model.Comment;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        Comment comment = new Comment();
        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            comment.setCommentator(user.getId());
        }else{
            //用户未登录
//            comment.setCommentator(999);
//            throw new CustomizeException(CustomizeErrorCode.LOGIN_NOT_FOUND);
            return CommentResultDTO.errorOf(CustomizeErrorCode.LOGIN_NOT_FOUND);
        }
        comment.setParentId(commentCreateDTO.getParentId());//注意这个ParentId可能不存在：发帖用户已删除
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(comment.getGmtModified());
        comment.setLikeCount(0L);
        commentService.insert(comment);
//        Map<Object,Object> objectObjectMap = new HashMap<>();
//        objectObjectMap.put("message","成功");
        return CommentResultDTO.okOf();
    }
}
