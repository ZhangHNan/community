package wanzhi.gulu.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import wanzhi.gulu.community.dto.QuestionDTO;
import wanzhi.gulu.community.model.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question")
    List<QuestionDTO> findDTOAll();

    @Select("select count(1) from question")//为什么用count（1）
    int findTotalCount();

    @Select("select * from question limit #{start},#{rows}")
    List<QuestionDTO> findByPage(@Param("start") int start, @Param("rows") Integer rows);

    @Select("select count(1) from question where creator = #{accountId}")
    Integer findTotalCountByCreator(@Param("accountId") int accountId);

    @Select("select * from question where creator = #{id} limit #{start},#{rows}")
    List<QuestionDTO> findByPageByCreator(@Param("start")int start,@Param("rows") int rows,@Param("id") Integer id);
}
