package cn.czcps.oa.service;

import cn.czcps.oa.dao.NoticeDao;
import cn.czcps.oa.entity.Notice;
import cn.czcps.oa.utils.MybatisUtils;

import java.util.List;

//通知公告
public class NoticeService {
    public List<Notice> getNoticeList(long receiverId)
    {
      return (List) MybatisUtils.executeUpdate(sqlSession -> {
            NoticeDao noticeDao=sqlSession.getMapper(NoticeDao.class);
            return  noticeDao.selectByReceiverId(receiverId);
        });
    }
}
