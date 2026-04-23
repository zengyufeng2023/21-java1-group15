package com.guet.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.User;
import com.guet.mapper.UserMapper;
import com.guet.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}
