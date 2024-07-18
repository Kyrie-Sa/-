package cn.czcps.oa.dao;

import cn.czcps.oa.entity.LeaveForm;
import cn.czcps.oa.entity.ProcessFlow;
import java.util.List;
public interface ProcessFlowDao {

    //按课程节数插入记录 （一节课程对应一组数据流）
    public void insert(ProcessFlow processFlow);

    //假条被审批后该流程结束（pending->complete）
    public void update(ProcessFlow processFlow);

    //查找该学生申请请假单的id
    public List<ProcessFlow> selectByFormId(long formId);
}
