package wanzhi.gulu.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.mapper.UserMapper;
import wanzhi.gulu.community.model.User;
import wanzhi.gulu.community.model.UserExample;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    //创建或更新用户：主要更新token
    public void createOrUpdate(User user){
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
//        User dbUser = userMapper.findByAccountId(user.getAccountId());
        //根据accountId判断数据库中有无这个user数据
//        if(dbUser == null){
        if(users.size() == 0){
            //如果没有，创建
//            userMapper.create(user);
            user.setGmtCreate(user.getGmtModified());
            userMapper.insert(user);
        }else{
            //如果有，更新Token
//            userMapper.update(user);
            UserExample userExample1 = new UserExample();
            userExample1.createCriteria()
                    .andIdEqualTo(users.get(0).getId());
            userMapper.updateByExampleSelective(user,userExample1);
        }
    }
}
