package wanzhi.gulu.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import wanzhi.gulu.community.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

//    @Select("select account_id from user where id = #{id}")
//    String findAccountIdById(@Param("id") Integer id);
//
//    @Select("select id from user where account_id = #{accountId}")
//    List<Integer> findAllIdByAccountId(@Param("accountId") String accountId);
}
