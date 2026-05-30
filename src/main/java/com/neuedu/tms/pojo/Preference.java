package com.neuedu.tms.pojo;

/**
 * 饮食偏好实体类，记录老人的个性化饮食偏好。
 * 字段：客户ID、饮食喜好、过敏食物、备注等。
 */
public class Preference {

    private int id;
    private int customerId;       // 顾客ID
    private String preferences;   // 饮食喜好（逗号分隔）
    private String allergies;     // 过敏食物
    private String remark;        // 备注
    private int isDeleted;        // 逻辑删除 0正常/1已删除

    public Preference() {
    }

    public Preference(int id, int customerId, String preferences, String allergies, String remark, int isDeleted) {
        this.id = id;
        this.customerId = customerId;
        this.preferences = preferences;
        this.allergies = allergies;
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

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
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
        return "Preference{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", preferences='" + preferences + '\'' +
                ", allergies='" + allergies + '\'' +
                ", remark='" + remark + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
