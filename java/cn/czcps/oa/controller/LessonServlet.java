package cn.czcps.oa.controller;

import cn.czcps.oa.entity.Lesson;
import cn.czcps.oa.service.LessonService;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "LessonServlet", value = "/lesson/*")
public class LessonServlet extends HttpServlet {
    private LessonService lessonService = new LessonService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 提取请求的URL中的方法名
        String url = request.getRequestURI();
        String methodName = url.substring(url.lastIndexOf("/") + 1);


        // 根据方法名调用相应的方法
        if (methodName.equals("getByDateRange")) {
            try {
                this.getByDateRange(request, response);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }



    // 根据某个学生请假的日期范围获取lesson
    private void getByDateRange(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        //接收各项请假单数据
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
        java.util.Date utilStartDate = formatter.parse(request.getParameter("startDate"));
        java.util.Date utilEndDate = formatter.parse(request.getParameter("endDate"));

        // 将util包的日期转换为sql包的日期
        java.sql.Date startDate = new java.sql.Date(utilStartDate.getTime());
        java.sql.Date endDate = new java.sql.Date(utilEndDate.getTime());


        // Get lessons by date range
        List<Lesson> lessons = lessonService.getLessonsByDateRange(startDate, endDate);
        Map result = new HashMap();
        result.put("code", 0);
        result.put("msg", "");
        result.put("data", lessons);


        // Convert lessons to JSON
        String json = JSON.toJSONString(result);

        // Write JSON to response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();// 刷新输出流
    }
}
