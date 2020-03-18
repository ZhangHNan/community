package wanzhi.gulu.community.mapper;

import wanzhi.gulu.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);
}