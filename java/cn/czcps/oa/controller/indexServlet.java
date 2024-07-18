package cn.czcps.oa.controller;

import cn.czcps.oa.entity.Department;
import cn.czcps.oa.entity.Employee;
import cn.czcps.oa.entity.Node;
import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.DepartmentService;
import cn.czcps.oa.service.EmployeeService;
import cn.czcps.oa.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
/*
* 用户登录后,
* 通过用户的信息
* 确定用户的角色及权限
* 以此来确定登录后进入对应的页面
* */

@WebServlet(name = "indexServlet", value = "/index")
public class indexServlet extends HttpServlet {
    private UserService userService=new UserService();
    private EmployeeService employeeService =new EmployeeService();
    private DepartmentService departmentService =new DepartmentService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session=request.getSession();

    User user=(User) session.getAttribute("login_user");

        Employee employee=(Employee) employeeService.selectById(user.getEmployeeId());


        Department department=(Department) departmentService.selectById(employee.getDepartmentId());

        // 查询用户的所有节点权限
        List<Node>nodeList= userService.selectNodeByUserId(user.getUserId());

        // 设置节点列表为请求属性，供JSP页面使用
        request.setAttribute("node_list",nodeList);


        // 设置当前员工和部门信息为会话属性，以便后续请求可以访问
        session.setAttribute("current_employee",employee);
        session.setAttribute("current_department",department);


        // 将请求转发到主页JSP
        request.getRequestDispatcher("/index.jsp").forward(request,response);


    }



    // 处理POST请求，这里直接调用doGet方法处理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
    }
}
