package com.neuedu.tms.pojo;

/**
 * 外出登记实体类，记录老人外出申请及审核信息。
 * 字段：客户ID、外出原因、外出时间、预计返回时间、实际返回时间、陪同人信息、审核状态等。
 */
public class Outward {

    private int id;
    private String remarks;
    private int customerId;
    private String outgoingReason;
    private String outgoingTime;          // yyyy-MM-dd HH:mm
    private String expectedReturnTime;    // yyyy-MM-dd HH:mm
    private String actualReturnTime;      // yyyy-MM-dd HH:mm
    private String escorted;              // 陪同人
    private String relation;              // 陪同人关系
    private String escortedTel;           // 陪同人电话
    private int auditStatus;              // 0-已提交, 1-同意, 2-拒绝
    private String auditPerson;           // 审核人
    private String auditTime;             // yyyy-MM-dd HH:mm
    private int isDeleted;                // 0-正常, 1-已删除

    public Outward() {
    }

    public Outward(int id, String remarks, int customerId, String outgoingReason,
                   String outgoingTime, String expectedReturnTime, String actualReturnTime,
                   String escorted, String relation, String escortedTel,
                   int auditStatus, String auditPerson, String auditTime, int isDeleted) {
        this.id = id;
        this.remarks = remarks;
        this.customerId = customerId;
        this.outgoingReason = outgoingReason;
        this.outgoingTime = outgoingTime;
        this.expectedReturnTime = expectedReturnTime;
        this.actualReturnTime = actualReturnTime;
        this.escorted = escorted;
        this.relation = relation;
        this.escortedTel = escortedTel;
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

    public String getOutgoingReason() {
        return outgoingReason;
    }

    public void setOutgoingReason(String outgoingReason) {
        this.outgoingReason = outgoingReason;
    }

    public String getOutgoingTime() {
        return outgoingTime;
    }

    public void setOutgoingTime(String outgoingTime) {
        this.outgoingTime = outgoingTime;
    }

    public String getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(String expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime;
    }

    public String getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(String actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public String getEscorted() {
        return escorted;
    }

    public void setEscorted(String escorted) {
        this.escorted = escorted;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEscortedTel() {
        return escortedTel;
    }

    public void setEscortedTel(String escortedTel) {
        this.escortedTel = escortedTel;
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
        return "Outward{" +
                "id=" + id +
                ", remarks='" + remarks + '\'' +
                ", customerId=" + customerId +
                ", outgoingReason='" + outgoingReason + '\'' +
                ", outgoingTime='" + outgoingTime + '\'' +
                ", expectedReturnTime='" + expectedReturnTime + '\'' +
                ", actualReturnTime='" + actualReturnTime + '\'' +
                ", escorted='" + escorted + '\'' +
                ", relation='" + relation + '\'' +
                ", escortedTel='" + escortedTel + '\'' +
                ", auditStatus=" + auditStatus +
                ", auditPerson='" + auditPerson + '\'' +
                ", auditTime='" + auditTime + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
