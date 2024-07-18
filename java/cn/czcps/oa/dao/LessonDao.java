package cn.czcps.oa.dao;

import cn.czcps.oa.entity.Lesson;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;



public interface LessonDao {
    @Select("SELECT * FROM lesson")
    List<Lesson> getAllLessons();
    @Select("SELECT * FROM lesson WHERE date BETWEEN #{startDate} AND #{endDate}")
    List<Lesson> getLessonsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Select("SELECT * FROM lesson WHERE id = #{id}")
    Lesson getLessonById(int id);

    @Insert("INSERT INTO lesson(id, name, teacher_id, status, date, class_num) VALUES(#{id}, #{name}, #{teacherId}, #{status}, #{date}, #{classNum})")
    void insertLesson(Lesson lesson);

    @Update("UPDATE lesson SET name = #{name}, teacher_id = #{teacherId}, status = #{status}, date = #{date}, class_num = #{classNum} WHERE id = #{id}")
    void updateLesson(Lesson lesson);

    @Delete("DELETE FROM lesson WHERE id = #{id}")
    void deleteLesson(int id);
    @Select("SELECT teacher_id FROM lesson WHERE id = #{id}")

    int selectTeahcerIdByLessonId(int id);
}
