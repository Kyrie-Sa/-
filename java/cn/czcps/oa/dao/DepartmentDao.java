package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Department;


public interface DepartmentDao {
    public Department selectById(long departmentId);
}
