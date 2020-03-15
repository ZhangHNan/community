package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(User user){
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        //根据accountId判断数据库中有无这个user数据
        if(dbUser == null){
            //如果没有，创建
            userMapper.create(user);
        }else{
            //如果有，更新Token
            userMapper.update(user);
        }
    }
}
