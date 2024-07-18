package cn.czcps.oa.service;

import cn.czcps.oa.dao.LeaveFormDao;
import cn.czcps.oa.dao.LessonDao;
import cn.czcps.oa.entity.Lesson;
import cn.czcps.oa.utils.MybatisUtils;

import java.sql.Date;
import java.util.List;
import java.util.Map;


public class LessonService {
//从数据库中获取所有课程
    public List<Lesson> getAllLessons() {

        return null;
    }



    //根据请假的时间段，从数据库中找出该时间段内所有的课程
    public List<Lesson> getLessonsByDateRange(Date startDate, Date endDate) {
        return (List<Lesson>) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(LessonDao.class).getLessonsByDateRange(startDate, endDate));

    }
}