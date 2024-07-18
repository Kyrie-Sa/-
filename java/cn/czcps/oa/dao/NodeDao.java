package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Node;
import cn.czcps.oa.utils.MybatisUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
//查询每个用户所对应的角色以及其对应的权限
public interface NodeDao {
    @Select("        SELECT distinct n.*\n" +
            "        FROM  sys_role_user ru,sys_role_node rn,sys_node n\n" +
            "        where ru.role_id=rn.role_id and rn.node_id=n.node_id and ru.user_id=#{userId}")
    public List<Node>selectNodeByUserId(@Param("userId") Long userId);
}
