package com.cc.service;

import com.cc.dto.EmployeeDepartmentDto;
import com.cc.entity.Attendance;
import com.cc.entity.Department;
import com.cc.entity.Employee;
import com.cc.entity.PositionRank;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public interface EmployeeService {
  //  员工列表方法
//  List<Employee> lists();

  List<EmployeeDepartmentDto> getEmployeeWithDepartments();
  List<PositionRank> findPositionRank();
  List<Department> findDepartments();
  //  保存员工信息

  void save(Employee employee);
  //  根据id查询一个

  Employee findById(Integer employee_id);
  //  更新员工信息

  boolean update(Employee employee);
  //    删除员工信息

  void delete(Integer employee_id);
  //  搜索

  List<EmployeeDepartmentDto> search(Integer employeeId, String employeeName, String department, String address);

  boolean isEmployeeValid(Integer employee_id);


  boolean isEmployeeAgeValid(LocalDate birth_date);


  boolean isPasswordValid(Integer employeeId, String password);

  List<Attendance> getAllAttendances(Integer employeeId);

  List<Attendance> searchDate(Integer employeeId, Integer year, Integer month, Integer day);
}
