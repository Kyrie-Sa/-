package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Notice;

import java.util.List;

//查找公告接收者&插入公告
public interface NoticeDao {
    public void insert(Notice notice);
    public List<Notice> selectByReceiverId(long receiverId);
}
