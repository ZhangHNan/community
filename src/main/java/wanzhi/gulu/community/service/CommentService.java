package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;
import wanzhi.gulu.community.mapper.CommentMapper;
import wanzhi.gulu.community.model.Comment;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    public void insert(Comment comment) {
        if(comment.getParentId()==null || comment.getParentId() ==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        commentMapper.insert(comment);
    }
}
