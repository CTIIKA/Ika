package com.cc.dao;

import com.cc.entity.User;

public interface UserDao {

//  根据用户查询用户
  User findByUserName(String user_name);
//  保存用户信息
  void save(User user);
}
