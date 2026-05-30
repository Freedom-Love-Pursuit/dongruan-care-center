package com.neuedu.tms.pojo;

/**
 * 客户护理项目购买记录实体类，记录客户购买的护理服务。
 * 字段：护理项目ID、客户ID、等级ID、购买数量、购买时间、到期时间等。
 */
public class CustomerNurseItem {

    private int id;
    private int itemId;
    private int customerId;
    private Integer levelId;
    private int nurseNumber;
    private String buyTime;        // yyyy-MM-dd
    private String maturityTime;   // yyyy-MM-dd
    private int isDeleted;         // 0-正常, 1-已删除

    public CustomerNurseItem() {
    }

    public CustomerNurseItem(int id, int itemId, int customerId, Integer levelId,
                             int nurseNumber, String buyTime, String maturityTime, int isDeleted) {
        this.id = id;
        this.itemId = itemId;
        this.customerId = customerId;
        this.levelId = levelId;
        this.nurseNumber = nurseNumber;
        this.buyTime = buyTime;
        this.maturityTime = maturityTime;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public int getNurseNumber() {
        return nurseNumber;
    }

    public void setNurseNumber(int nurseNumber) {
        this.nurseNumber = nurseNumber;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getMaturityTime() {
        return maturityTime;
    }

    public void setMaturityTime(String maturityTime) {
        this.maturityTime = maturityTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "CustomerNurseItem{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", customerId=" + customerId +
                ", levelId=" + levelId +
                ", nurseNumber=" + nurseNumber +
                ", buyTime='" + buyTime + '\'' +
                ", maturityTime='" + maturityTime + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
