package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Node;
import cn.czcps.oa.utils.MybatisUtils;

import java.util.List;


//查用户对应的角色
public interface RoleDao {
    public String selectRoleNameByUserId(long userId);

}
