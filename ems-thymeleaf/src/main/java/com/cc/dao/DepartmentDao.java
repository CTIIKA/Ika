package com.cc.dao;
import com.cc.entity.Department;

import java.util.List;

public interface DepartmentDao {
  List<Department> findAll();
}
