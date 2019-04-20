package com.qf.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.common.MD5Utils;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.sql.Wrapper;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public int insertUser(User user) {
        String pwd=MD5Utils.md5(user.getPassword());
        user.setPassword(pwd);
        return userMapper.insert(user);
    }

    @Override
    public User loginUser(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", MD5Utils.md5(password));

        User user = userMapper.selectOne(queryWrapper);

        return user;
    }

    //激活用户
    @Override
    public int activeUser(String username) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        user.setStatus(1);

        userMapper.updateById(user);
        return 1;
    }
}
