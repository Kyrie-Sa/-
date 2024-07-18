package cn.czcps.oa.dao;

import cn.czcps.oa.entity.LeaveForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
public interface LeaveFormDao {
    public void insert(LeaveForm leaveForm);
//    "pf_state"是请假流程的完成状态（penging~正在审批，complete~已审批）
//    "pf_operator_id"是审批人id
    public List<Map> selectByParams(@Param("pf_state") String pfState,@Param("pf_operator_id") long operatorId);
    public void update(LeaveForm leaveForm);
    public LeaveForm selectById(Long formId);
}
