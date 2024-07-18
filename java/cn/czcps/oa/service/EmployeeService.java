package cn.czcps.oa.service;

import cn.czcps.oa.dao.EmployeeDao;
import cn.czcps.oa.entity.Employee;
import cn.czcps.oa.utils.MybatisUtils;


//通过用户ID获取用户信息
public class EmployeeService {
  public Employee selectById(long employeeId){
    return  (Employee)MybatisUtils.executeQuery(sqlSession -> {
      return sqlSession.getMapper(EmployeeDao.class).selectById(employeeId);
    });
  }
}
