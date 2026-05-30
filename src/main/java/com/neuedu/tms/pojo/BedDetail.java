package com.neuedu.tms.pojo;

/**
 * 床位详情实体类，记录床位使用历史详情。
 * 字段：客户ID、床位ID、使用开始日期、结束日期、备注等。
 */
public class BedDetail {

    private int id;
    private int customerId;       // 顾客ID
    private int bedId;            // 床位ID
    private String startDate;     // 使用开始日期
    private String endDate;       // 使用结束日期（null=仍在用）
    private String remark;        // 备注
    private int isDeleted;        // 逻辑删除 0正常/1已删除

    public BedDetail() {
    }

    public BedDetail(int id, int customerId, int bedId, String startDate, String endDate, String remark, int isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.bedId = bedId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remark = remark;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "BedDetail{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", bedId=" + bedId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", remark='" + remark + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
