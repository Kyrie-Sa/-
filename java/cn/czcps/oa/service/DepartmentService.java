package cn.czcps.oa.service;

import cn.czcps.oa.dao.DepartmentDao;
import cn.czcps.oa.entity.Department;
import cn.czcps.oa.utils.MybatisUtils;


//通过用户ID获取用户所在院系
public class DepartmentService {
  public Department selectById(long departmentId){
    return  (Department) MybatisUtils.executeQuery(sqlSession -> {
      return sqlSession.getMapper(DepartmentDao.class).selectById(departmentId);
    });
  }
}
