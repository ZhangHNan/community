package wanzhi.gulu.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wanzhi.gulu.community.dto.CommentCreateDTO;
import wanzhi.gulu.community.dto.CommentDTO;
import wanzhi.gulu.community.dto.CommentResultDTO;
import wanzhi.gulu.community.enums.CommentTypeEnum;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.model.Comment;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 前后端分离API（Ajax）：前端携带Json数据请求（contentType:"application/json",data:JSON.stringify(),dataType:"json"），
     * 后端返回json数据
     * @param commentCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        Comment comment = new Comment();
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            //用户未登录
//            comment.setCommentator(999);
//            throw new CustomizeException(CustomizeErrorCode.LOGIN_NOT_FOUND);
            return CommentResultDTO.errorOf(CustomizeErrorCode.LOGIN_NOT_FOUND);
        }
//        if(commentCreateDTO.getContent()==null||"".equals(commentCreateDTO.getContent())){
        if(StringUtils.isBlank(commentCreateDTO.getContent())){
            return CommentResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        comment.setCommentator(user.getId());
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

    //获取二级评论列表API
    @ResponseBody
    @GetMapping("/comment/{id}")
    public CommentResultDTO comments(@PathVariable("id")Long id){
        List<CommentDTO> commentDTOs = commentService.listByTargetId(id,CommentTypeEnum.COMMENT);
        return CommentResultDTO.okOf(commentDTOs);
    }
}
