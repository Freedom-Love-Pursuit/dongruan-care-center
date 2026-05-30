package com.neuedu.tms.pojo;

/**
 * 护理内容实体类，定义可提供的护理服务项目。
 * 字段：编号、护理名称、服务单价、说明、状态、执行周期、执行次数等。
 */
public class NurseContent {

    private int id;
    private String serialNumber;
    private String nursingName;
    private String servicePrice;
    private String message;
    private int status;           // 1-启用, 2-停用
    private String executionCycle;
    private String executionTimes;
    private int isDeleted;        // 0-正常, 1-已删除

    public NurseContent() {
    }

    public NurseContent(int id, String serialNumber, String nursingName, String servicePrice,
                        String message, int status, String executionCycle,
                        String executionTimes, int isDeleted) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.nursingName = nursingName;
        this.servicePrice = servicePrice;
        this.message = message;
        this.status = status;
        this.executionCycle = executionCycle;
        this.executionTimes = executionTimes;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNursingName() {
        return nursingName;
    }

    public void setNursingName(String nursingName) {
        this.nursingName = nursingName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExecutionCycle() {
        return executionCycle;
    }

    public void setExecutionCycle(String executionCycle) {
        this.executionCycle = executionCycle;
    }

    public String getExecutionTimes() {
        return executionTimes;
    }

    public void setExecutionTimes(String executionTimes) {
        this.executionTimes = executionTimes;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "NurseContent{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", nursingName='" + nursingName + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", executionCycle='" + executionCycle + '\'' +
                ", executionTimes='" + executionTimes + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
