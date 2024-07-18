package cn.czcps.oa.service;

import cn.czcps.oa.dao.*;
import cn.czcps.oa.entity.Employee;
import cn.czcps.oa.entity.LeaveForm;
import cn.czcps.oa.entity.Notice;
import cn.czcps.oa.entity.ProcessFlow;
import cn.czcps.oa.service.exception.BussinessException;
import cn.czcps.oa.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveFormService {

    // 创建请假申请表单

    public LeaveForm createLeaveForm(LeaveForm form, List<String> courseIds) {
        LeaveForm savedForm = (LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {


            Employee employee = sqlSession.getMapper(EmployeeDao.class).selectById(form.getEmployeeId());
            form.setState("processing");//学生提交申请请假后,设置状态为processing
            sqlSession.getMapper(LeaveFormDao.class).insert(form);//将其插入到学生请假记录表中


            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // 针对某学生申请请假的每门课程生成审批流程
            for (String courseId : courseIds) {

                /*
                * 查找该学生所对应学院的辅导员
                * */
                Employee counselor = sqlSession.getMapper(EmployeeDao.class).selectByDepartmentAndLevel(employee.getDepartmentId());

                if (counselor == null) {
                    throw new IllegalStateException("该学院的辅导员不存在");
                }

                //通过申请请假的课程查找对应的代课老师
                int teacherId = sqlSession.getMapper(LessonDao.class).selectTeahcerIdByLessonId(Integer.parseInt(courseId));

                //设置流程表的字段值
                ProcessFlow flow = new ProcessFlow();
                flow.setState("pending");
                flow.setFormId(form.getFormId());
                flow.setCreateTime(new Date());
                flow.setOperatorId(counselor.getEmployeeId());
                flow.setAction("audit");
                flow.setLessonId(Integer.parseInt(courseId) );
                //插入到流程表中
                sqlSession.getMapper(ProcessFlowDao.class).insert(flow);



                String noticeContent = String.format("%s-%s 提起请假申请[%s-%s]针对课程ID为%s，等待您的审批.",
                        employee.getTitle(), employee.getName(),
                        simpleDateFormat.format(form.getStartTime()), simpleDateFormat.format(form.getEndTime()),
                        courseId);
                noticeDao.insert(new Notice(counselor.getEmployeeId(), noticeContent));


            }

            String applicantNotice = String.format("您的请假申请[%s-%s]已提交，请等待审批.",
                    simpleDateFormat.format(form.getStartTime()), simpleDateFormat.format(form.getEndTime()));
            noticeDao.insert(new Notice(employee.getEmployeeId(), applicantNotice));

               return form;
             });

        return savedForm;
    }



    // 获取请假申请列表
    public List<Map> getLeaveFormList(String pfState, Long operatorId){
       return (List<Map>) MybatisUtils.executeQuery(sqlSession ->
               sqlSession.getMapper(LeaveFormDao.class).selectByParams(pfState,operatorId));
    }




    // 审批请假申请
    public void audit(long formId, long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            LeaveForm form = leaveFormDao.selectById(formId);
            Employee employee = employeeDao.selectById(form.getEmployeeId());

            List<ProcessFlow> processFlows = processFlowDao.selectByFormId(formId);

         /*    获取审批流程列表
             filter操作过滤出所有ProcessFlow对象，其中operatorId与提供的operatorId匹配，并且状态是"pending"。
             这意味着只考虑那些等待当前操作员审批的流程。
         */
            processFlows.stream()
                    .filter(pf -> pf.getOperatorId() == operatorId && "pending".equals(pf.getState()))
                    .findFirst()
                    .ifPresentOrElse(processFlow -> {
                        //更新流程表字段信息
                        processFlow.setState("complete");
                        processFlow.setResult(result);
                        processFlow.setReason(reason);
                        processFlow.setAuditTime(new Date());
                        processFlowDao.update(processFlow);

                        form.setState(result);
                        leaveFormDao.update(form);

                        String resultText = result.equals("approved") ? "批准" : "驳回";
                        String noticeContent = String.format("您的请假申请[%s-%s]已%s, 审批意见：%s,审批流程已结束",
                                simpleDateFormat.format(form.getStartTime()), simpleDateFormat.format(form.getEndTime()), resultText, reason);
                        noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent));

                        //如果节课的假被审批同意则转发给老师
                        if("approved".equals(result)){
                            int teacherId = sqlSession.getMapper(LessonDao.class).selectTeahcerIdByLessonId(processFlow.getLessonId());
                            String teacherContent = String.format("%s-%s 提起请假申请[%s-%s]针对课程ID为%s，已经被审批成功.",
                                    employee.getTitle(), employee.getName(),
                                    simpleDateFormat.format(form.getStartTime()), simpleDateFormat.format(form.getEndTime()),
                                    processFlow.getLessonId());
                            //将审批成功的结果添加到公告里
                            noticeDao.insert(new Notice(teacherId, teacherContent));
                        }

                    }, () -> {
                        throw new RuntimeException("未找到待处理任务或任务已完成");
                    });

            return null;
        });
    }

}
