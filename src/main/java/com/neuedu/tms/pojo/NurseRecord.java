package com.neuedu.tms.pojo;

/**
 * 护理记录实体类，记录护理人员对客户执行的护理操作日志。
 * 字段：客户ID、护理项目ID、护理时间、护理内容、护理次数、执行人ID等。
 */
public class NurseRecord {

    private int id;
    private int customerId;
    private int itemId;
    private String nursingTime;      // yyyy-MM-dd HH:mm
    private String nursingContent;
    private int nursingCount;
    private int userId;
    private int isDeleted;           // 0-正常, 1-已删除

    public NurseRecord() {
    }

    public NurseRecord(int id, int customerId, int itemId, String nursingTime,
                       String nursingContent, int nursingCount, int userId, int isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.itemId = itemId;
        this.nursingTime = nursingTime;
        this.nursingContent = nursingContent;
        this.nursingCount = nursingCount;
        this.userId = userId;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getNursingTime() {
        return nursingTime;
    }

    public void setNursingTime(String nursingTime) {
        this.nursingTime = nursingTime;
    }

    public String getNursingContent() {
        return nursingContent;
    }

    public void setNursingContent(String nursingContent) {
        this.nursingContent = nursingContent;
    }

    public int getNursingCount() {
        return nursingCount;
    }

    public void setNursingCount(int nursingCount) {
        this.nursingCount = nursingCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "NurseRecord{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", itemId=" + itemId +
                ", nursingTime='" + nursingTime + '\'' +
                ", nursingContent='" + nursingContent + '\'' +
                ", nursingCount=" + nursingCount +
                ", userId=" + userId +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
