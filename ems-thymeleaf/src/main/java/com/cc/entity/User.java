package com.cc.entity;

import javax.validation.constraints.NotEmpty;

public class User {
  private Integer user_id;
  @NotEmpty(message = "ユーザ名を入力してください!")
  private String user_name;
  @NotEmpty(message = "パスワードを入力してください!")
  private String password;

  public Integer getUser_id() {
    return user_id;
  }

  public void setUser_id(Integer user_id) {
    this.user_id = user_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User() {
  }

  public User(Integer user_id, String user_name, String password) {
    this.user_id = user_id;
    this.user_name = user_name;
    this.password = password;
  }
}


