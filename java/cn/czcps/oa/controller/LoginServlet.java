package cn.czcps.oa.controller;

import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.UserService;
import cn.czcps.oa.service.exception.BussinessException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "LoginServlet", value = "/check_login")
public class LoginServlet extends HttpServlet {
    Logger logger= LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService=new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Map <String,Object>result=new HashMap<>();
        try {
            User user= userService.checkLogin(username,password);
            //创建session（“login_user”，每次用户登录时将登录者的信息存到会话里面供全局使用）
            HttpSession session=request.getSession();
            session.setAttribute("login_user",user);
            result.put("code","0");
            result.put("message","success");
            result.put("redirect_url","/index");
        } catch (BussinessException e) {
            logger.error(e.getMessage(),e);
            result.put("code",e.getCode());
            result.put("message",e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());
        }
       String json= JSON.toJSONString(result);
        response.getWriter().println(json);
    }
}
