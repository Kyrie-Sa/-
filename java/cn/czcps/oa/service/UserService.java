package cn.czcps.oa.service;

import cn.czcps.oa.dao.NodeDao;
import cn.czcps.oa.dao.RoleDao;
import cn.czcps.oa.dao.UserDao;
import cn.czcps.oa.entity.Node;
import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.exception.BussinessException;
import cn.czcps.oa.utils.MybatisUtils;

import java.util.List;

public class UserService {


    //登录判断逻辑
    public User checkLogin(String username, String password){
        User user =(User) MybatisUtils.executeQuery(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            return userDao.selectByUsername(username);
        });

        if(user==null){
            throw new BussinessException("s001","用户不存在");
        }
        if(!password.equals(user.getPassword())){
            throw new BussinessException("s002","密码错误");
        }
        return user;
    }


    //查找登录用户的节点权限 行政审批->{通知公告 请假申请 请假审批}
    public List<Node> selectNodeByUserId(Long userId){
        return (List<Node>) MybatisUtils.executeQuery(sqlSession ->
                sqlSession.getMapper(NodeDao.class).selectNodeByUserId(userId));
    }


    //查找登录用户所对应的角色
    public String selectRoleNameByUserId(long userId) {
        return (String) MybatisUtils.executeQuery(sqlSession ->
             sqlSession.getMapper(RoleDao.class).selectRoleNameByUserId(userId));
    }


    //修改用户信息
    public void updateUserProfile(long userId, String nickname, String password) {
        User user =(User) MybatisUtils.executeQuery(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            return userDao.selectByUserId(userId);
        });
        if (user == null) {
            throw new BussinessException("s001", "用户不存在");
        }
        if (nickname != null && !nickname.isEmpty()) {
            user.setUsername(nickname);
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        MybatisUtils.executeUpdate(sqlSession -> {
            UserDao userDao = sqlSession.getMapper(UserDao.class);
            userDao.update(user);
            return null;
        });
    }
}
