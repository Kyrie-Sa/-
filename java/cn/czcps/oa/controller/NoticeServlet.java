package cn.czcps.oa.controller;

import cn.czcps.oa.entity.Notice;
import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.NoticeService;
import com.alibaba.fastjson.JSON;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "NoticeServlet", value = "/notice/list")
public class NoticeServlet extends HttpServlet {
    private NoticeService noticeService=new NoticeService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 从会话中获取已登录用户的信息
        User user=(User) request.getSession().getAttribute("login_user");

        // 调用 NoticeService 获取与用户 ID 相关的通知列表
        List<Notice> noticeList=noticeService.getNoticeList(user.getEmployeeId());

        // 创建一个映射来保存结果数据
        Map result=new HashMap();
        result.put("code",0);
        result.put("msg","");
        result.put("count",noticeList.size());// 获取的通知数量
        result.put("data",noticeList);// 将通知放入"data"
      String json=  JSON.toJSONString(result);
      response.setContentType("text/html;charset=utf-8");
      response.getWriter().println(json);
    }

}
