package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//将提出申请请假的该学生及所在学院的辅导员对应在一起

public interface EmployeeDao {

    public Employee selectById(Long employeeId);
    public Employee selectLeader(@Param("emp") Employee employee);

    @Select("SELECT * FROM adm_employee WHERE department_id = #{departmentId} AND title = '辅导员' LIMIT 1")
    Employee selectByDepartmentAndLevel(@Param("departmentId") Long departmentId);
}
