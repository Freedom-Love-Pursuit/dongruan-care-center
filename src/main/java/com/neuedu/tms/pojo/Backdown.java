package com.neuedu.tms.pojo;

/**
 * 退住登记实体类，记录老人退住申请及审核信息。
 * 字段：客户ID、退住时间、退住类型、退住原因、审核状态、审核人、审核时间等。
 */
public class Backdown {

    private int id;
    private String remarks;
    private int customerId;
    private String retreatTime;      // yyyy-MM-dd HH:mm
    private int retreatType;         // 0-正常, 1-死亡, 2-保留
    private String retreatReason;
    private int auditStatus;         // 0-已提交, 1-同意, 2-拒绝
    private String auditPerson;      // 审核人
    private String auditTime;        // yyyy-MM-dd HH:mm
    private int isDeleted;           // 0-正常, 1-已删除

    public Backdown() {
    }

    public Backdown(int id, String remarks, int customerId, String retreatTime,
                    int retreatType, String retreatReason, int auditStatus,
                    String auditPerson, String auditTime, int isDeleted) {
        this.id = id;
        this.remarks = remarks;
        this.customerId = customerId;
        this.retreatTime = retreatTime;
        this.retreatType = retreatType;
        this.retreatReason = retreatReason;
        this.auditStatus = auditStatus;
        this.auditPerson = auditPerson;
        this.auditTime = auditTime;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getRetreatTime() {
        return retreatTime;
    }

    public void setRetreatTime(String retreatTime) {
        this.retreatTime = retreatTime;
    }

    public int getRetreatType() {
        return retreatType;
    }

    public void setRetreatType(int retreatType) {
        this.retreatType = retreatType;
    }

    public String getRetreatReason() {
        return retreatReason;
    }

    public void setRetreatReason(String retreatReason) {
        this.retreatReason = retreatReason;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Backdown{" +
                "id=" + id +
                ", remarks='" + remarks + '\'' +
                ", customerId=" + customerId +
                ", retreatTime='" + retreatTime + '\'' +
                ", retreatType=" + retreatType +
                ", retreatReason='" + retreatReason + '\'' +
                ", auditStatus=" + auditStatus +
                ", auditPerson='" + auditPerson + '\'' +
                ", auditTime='" + auditTime + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
