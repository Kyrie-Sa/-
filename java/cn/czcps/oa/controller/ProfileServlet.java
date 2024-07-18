package cn.czcps.oa.controller;

import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.LeaveFormService;
import cn.czcps.oa.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profileEdit"})
public class ProfileServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从请求中获取参数
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        // 获取当前用户的会话
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("login_user");

        if (currentUser != null) {
            try {
                // 更新用户资料
                userService.updateUserProfile(currentUser.getUserId(), nickname, password);
                // 用户资料更新成功后，注销当前会话
                session.invalidate();
                // 重定向到登录页面
                response.sendRedirect("login.html");
            } catch (Exception e) {
                request.setAttribute("error", "更新失败: " + e.getMessage());
                request.getRequestDispatcher("/profileEdit.jsp").forward(request, response);
            }
        } else {
            // 如果用户未登录或会话已过期
            response.sendRedirect("login.html");
        }
    }


}
