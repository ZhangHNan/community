package wanzhi.gulu.community.mapper;

import wanzhi.gulu.community.dto.SearchDTO;
import wanzhi.gulu.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question record);

    Integer countBySearch(String regxpSearch);

    List<Question> selectBySearchPage(SearchDTO searchDTO);
}