package com.cc.entity;

public class PositionRank {
  Integer position_id;
  String position_name;

  public PositionRank() {
  }

  public PositionRank(Integer position_id, String position_name) {
    this.position_id = position_id;
    this.position_name = position_name;
  }

  public Integer getPosition_id() {
    return position_id;
  }

  public void setPosition_id(Integer position_id) {
    this.position_id = position_id;
  }

  public String getPosition_name() {
    return position_name;
  }

  public void setPosition_name(String position_name) {
    this.position_name = position_name;
  }
}
