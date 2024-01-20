package com.cc.service;

import com.cc.dao.DepartmentDao;
import com.cc.dao.EmployeeDao;
import com.cc.dao.PositionRankDao;
import com.cc.dto.EmployeeDepartmentDto;
import com.cc.entity.Attendance;
import com.cc.entity.Department;
import com.cc.entity.Employee;
import com.cc.entity.PositionRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

  private EmployeeDao employeeDao;
  private DepartmentDao departmentDao;
  private PositionRankDao positionRankDao;

  @Autowired
  public EmployeeServiceImpl(EmployeeDao employeeDao, DepartmentDao departmentDao, PositionRankDao positionRankDao) {
    this.employeeDao = employeeDao;
    this.departmentDao = departmentDao;
    this.positionRankDao = positionRankDao;
  }



  @Override
  public List<EmployeeDepartmentDto> search(Integer employee_id, String employee_name, String department, String address) {
    return employeeDao.search(employee_id,employee_name,department,address);
  }

  @Override
  public boolean isEmployeeValid(Integer employee_id) {
    return employeeDao.findById(employee_id) != null;
  }

  @Override
  public boolean isEmployeeAgeValid(LocalDate birth_date) {
    LocalDate current_date = LocalDate.now();
    Period age = Period.between(birth_date, current_date);
    int minAge = 18;
    int maxAge = 60;
    return age.getYears() < minAge || age.getYears() > maxAge;
  }

  @Override
  public boolean isPasswordValid(Integer employeeId, String password) {
    Employee employee = employeeDao.findById(employeeId);
    return employee!=null&&employee.getPassword().equals(password);
  }

  @Override
  public List<Attendance> getAllAttendances(Integer employeeId) {
    return employeeDao.getAllAttendances(employeeId);
  }

  @Override
  public List<Attendance> searchDate(Integer employeeId, Integer year, Integer month, Integer day) {
    return employeeDao.searchDate(employeeId,year,month,day);
  }

  @Override
  public void clock(Attendance attendance) {
    employeeDao.clock(attendance);
  }

  @Override
  public Attendance findByRecordId(Integer recordId) {
    return employeeDao.findByRecordId(recordId);
  }

  @Override
  public void updateAttendance(Attendance attendance) {
    employeeDao.updateAttendance(attendance);
  }


  @Override
  public void delete(Integer employee_id) {
    employeeDao.delete(employee_id);
  }

  @Override
  public boolean update(Employee employee) {
    employeeDao.update(employee);
    return false;
  }

  @Override
  public Employee findById(Integer employee_id) {
    return employeeDao.findById(employee_id);
  }

  @Override
  public List<EmployeeDepartmentDto> getEmployeeWithDepartments() {
    return employeeDao.getEmployeeWithDepartments();
  }

  @Override
  public List<PositionRank> findPositionRank() {
    return positionRankDao.findAll();
  }

  @Override
  public List<Department> findDepartments() {
    return departmentDao.findAll();
  }

  @Override
  public void save(Employee employee) {
    employeeDao.save(employee);
  }

}
