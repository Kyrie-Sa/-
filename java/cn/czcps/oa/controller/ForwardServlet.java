package cn.czcps.oa.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/*
  点击哪个功能模块就会跳转到到对用的模块上
  行政审批->{通知公告、请假申请、请假审批}
  直接在数据库中拿url
*/
@WebServlet(name = "ForwardServlet", value = "/forward/*")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getRequestURI();
        String subUri=uri.substring(1);
        String page=subUri.substring(subUri.lastIndexOf("/"));
        request.getRequestDispatcher(page+".jsp").forward(request,response);
    }

}
