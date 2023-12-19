package com.cc.service;

import com.cc.entity.User;

public interface UserService {
//  注册用户
  void register(User user);

  boolean isUserValid(String userName, String password);

  boolean isUserExisted(String userName);

  boolean isPasswordValid(String password);
}
