package cn.czcps.oa.controller;

import cn.czcps.oa.entity.LeaveForm;
import cn.czcps.oa.entity.User;
import cn.czcps.oa.service.LeaveFormService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet(name = "LeaveFormServlet", value = "/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService = new LeaveFormService();
    private Logger logger = LoggerFactory.getLogger(LeaveFormServlet.class);// 日志记录器


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8"); // 设置请求字符集编码
        response.setContentType("text/html;charset=utf-8");// 设置响应内容类型和字符集编码


        // 从请求URL中截取方法名称
        String url = request.getRequestURI();
        String methodName = url.substring(url.lastIndexOf("/") + 1);



        // 根据方法名称调用相应的方法
        if (methodName.equals("create")) {
            this.create(request, response);
        } else if (methodName.equals("list")) {
            this.getLeaveFormList(request, response);
        } else if (methodName.equals("audit")) {
            this.auditForm(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }



    // 创建请假表单
        private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

    //接收各种请假表单数据
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login_user");
        String formType = request.getParameter("formType");//请假类型(事假,病假,工伤假...)
        String strStartTime = request.getParameter("startTime");
        String strEndTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");
        List<String> courseIds = extractIndexedParameters(request, "lessons");

        LeaveForm form = new LeaveForm();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d"); // 创建日期格式化对象

        form.setEmployeeId(user.getEmployeeId());
        Map<String, Object> result = new HashMap<>();
        try {
            // 解析请假日期字符串
            form.setStartTime(sdf.parse(strStartTime));
            form.setEndTime(sdf.parse(strEndTime));

            //设置请假表单其他字段
            form.setFormType(Integer.parseInt(formType));
            form.setReason(reason);
            form.setCourseIds(courseIds);
            form.setCreateTime(new Date());

            // 调用业务逻辑方法创建请假表单
            leaveFormService.createLeaveForm(form,courseIds);
            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Leave application error", e);
            result.put("code", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
        }

        // 将结果Map转换为JSON字符串并输出
        String json = JSON.toJSONString(result);
        out.println(json);
    }



    // 获取请假表单列表
    private void getLeaveFormList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("login_user");

        // 调用业务逻辑获取待审批的请假表单列表
        List<Map> formList = leaveFormService.getLeaveFormList("pending", user.getEmployeeId());

        System.out.println("userId:" + user.getEmployeeId());

        Map result = new HashMap();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", formList.size());
        result.put("data", formList);

        String json = JSON.toJSONString(result);
        System.out.println(json);
        response.getWriter().println(json);


    }






    // 审批请假表单
    private void auditForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // 获取审批参数
        String formId = request.getParameter("formId");
        String reason = request.getParameter("reason");
        String result = request.getParameter("result");
        User user = (User) request.getSession().getAttribute("login_user");
        long operatorId = user.getEmployeeId();//审批人id
        Map mpresult = new HashMap();
        try {
            // 调用业务逻辑方法审批请假表单
            leaveFormService.audit(Long.parseLong(formId), operatorId, result, reason);

            mpresult.put("code", "0");
            mpresult.put("message", "success");
        } catch (Exception e) {
            logger.error("请假单审核失败", e);
            mpresult.put("code", e.getClass().getSimpleName());// 异常类名作为错误代码
            mpresult.put("message", e.getMessage()); // 异常消息
        }
        String json = JSON.toJSONString(mpresult);
        out.println(json);
    }


    // 辅助方法：从请求中提取索引参数
    private List<String> extractIndexedParameters(HttpServletRequest request, String baseParamName) {

        List<String> values = new ArrayList<>(); //这里把某一个学生请假表单里所涉及到的所有lesson组合为一个list
        int index = 0;


        // 循环提取参数值
        while (true) {
            String paramValue = request.getParameter(baseParamName + "[" + index + "]");
            if (paramValue == null) {
                break;
            }
            values.add(paramValue);
            index++;
        }
        return values;
    }


}
