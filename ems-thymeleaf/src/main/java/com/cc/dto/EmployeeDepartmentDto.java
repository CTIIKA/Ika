package com.cc.dto;

public class EmployeeDepartmentDto {
  Integer employee_id;
  String employee_name;
  String department_name;
  String address;
  String sex;

  public EmployeeDepartmentDto() {
  }

  public EmployeeDepartmentDto(Integer employee_id, String employee_name, String department_name, String address, String sex) {
    this.employee_id = employee_id;
    this.employee_name = employee_name;
    this.department_name = department_name;
    this.address = address;
    this.sex = sex;
  }

  public Integer getEmployee_id() {
    return employee_id;
  }

  public void setEmployee_id(Integer employee_id) {
    this.employee_id = employee_id;
  }

  public String getEmployee_name() {
    return employee_name;
  }

  public void setEmployee_name(String employee_name) {
    this.employee_name = employee_name;
  }

  public String getDepartment_name() {
    return department_name;
  }

  public void setDepartment_name(String department_name) {
    this.department_name = department_name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }
}
