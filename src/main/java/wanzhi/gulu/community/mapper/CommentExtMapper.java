package wanzhi.gulu.community.mapper;

import wanzhi.gulu.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}