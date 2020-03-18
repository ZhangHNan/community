package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.enums.CommentTypeEnum;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;
import wanzhi.gulu.community.mapper.CommentMapper;
import wanzhi.gulu.community.mapper.QuestionMapper;
import wanzhi.gulu.community.model.Comment;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    CommentMapper commentMapper;

    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId() ==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_ERROR);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
            //回复问题
            questionMapper.selectByPrimaryKey(comment.getParentId());
        }
        commentMapper.insert(comment);
    }
}
