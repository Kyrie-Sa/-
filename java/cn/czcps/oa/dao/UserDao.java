package cn.czcps.oa.dao;

import cn.czcps.oa.entity.User;
import cn.czcps.oa.utils.MybatisUtils;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
     User selectByUsername(String username);
     User selectByUserId(Long userId);

     void update(User user);

}
