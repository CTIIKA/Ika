package com.cc.dao;


import com.cc.dto.EmployeeDepartmentDto;
import com.cc.entity.Attendance;
import com.cc.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeDao {
//  员工列表
  List<EmployeeDepartmentDto> getEmployeeWithDepartments();
//    保存员工信息
  void save(Employee employee);
//  根据id查询一个
  Employee findById(Integer employee_id);
//  更新员工信息
  void update(Employee employee);
//  删除员工信息
  void delete(Integer employee_id);
//  搜索
  List<EmployeeDepartmentDto> search(
          @Param("employee_id") Integer employee_id,
          @Param("employee_name") String employee_name,
          @Param("department") String department,
          @Param("address") String address);

  List<Attendance> getAllAttendances(Integer employeeId);

  List<Attendance> searchDate(Integer employeeId, Integer year, Integer month, Integer day);
}
