package cn.czcps.oa.entity;

import java.util.Date;

public class Notice {
    private long noticeId;
    private long receriverId;
    private String content;
    private Date createTime;


    private int status;
    public Notice() {
    }
    public Notice(long receriverId, String content) {
        this.receriverId = receriverId;
        this.content = content;
        this.setCreateTime(new Date());
    }
    public Notice(long receriverId, String content,int status) {
        this.receriverId = receriverId;
        this.content = content;
        this.setCreateTime(new Date());
        this.status=status;
    }
    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public long getReceriverId() {
        return receriverId;
    }

    public void setReceriverId(long receriverId) {
        this.receriverId = receriverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
