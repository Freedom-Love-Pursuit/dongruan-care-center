package com.neuedu.tms.pojo;

/**
 * 客户实体类，记录入住老人的基本信息。
 * 字段：姓名、年龄、性别、身份证号、房间号、床位ID、护理等级、联系方式、身体状况、注意事项等。
 */
public class Customer {

    private int id;
    private String customerName;
    private int customerAge;
    private int customerSex;       // 0-男, 1-女
    private String idcard;
    private String roomNo;
    private String buildingNo;
    private String checkinDate;    // yyyy-MM-dd
    private String expirationDate; // yyyy-MM-dd
    private String contactTel;
    private int bedId;
    private String psychosomaticState;
    private String attention;
    private String birthday;       // yyyy-MM-dd
    private String height;
    private String weight;
    private String bloodType;
    private Integer userId;        // 管家ID, null=无
    private Integer levelId;       // 护理等级ID
    private String familyMember;
    private int isDeleted;         // 0-正常, 1-已删除

    public Customer() {
    }

    public Customer(int id, String customerName, int customerAge, int customerSex,
                    String idcard, String roomNo, String buildingNo, String checkinDate,
                    String expirationDate, String contactTel, int bedId,
                    String psychosomaticState, String attention, String birthday,
                    String height, String weight, String bloodType, Integer userId,
                    Integer levelId, String familyMember, int isDeleted) {
        this.id = id;
        this.customerName = customerName;
        this.customerAge = customerAge;
        this.customerSex = customerSex;
        this.idcard = idcard;
        this.roomNo = roomNo;
        this.buildingNo = buildingNo;
        this.checkinDate = checkinDate;
        this.expirationDate = expirationDate;
        this.contactTel = contactTel;
        this.bedId = bedId;
        this.psychosomaticState = psychosomaticState;
        this.attention = attention;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.userId = userId;
        this.levelId = levelId;
        this.familyMember = familyMember;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }

    public int getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(int customerSex) {
        this.customerSex = customerSex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public String getPsychosomaticState() {
        return psychosomaticState;
    }

    public void setPsychosomaticState(String psychosomaticState) {
        this.psychosomaticState = psychosomaticState;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerAge=" + customerAge +
                ", customerSex=" + customerSex +
                ", idcard='" + idcard + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", checkinDate='" + checkinDate + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", contactTel='" + contactTel + '\'' +
                ", bedId=" + bedId +
                ", psychosomaticState='" + psychosomaticState + '\'' +
                ", attention='" + attention + '\'' +
                ", birthday='" + birthday + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", userId=" + userId +
                ", levelId=" + levelId +
                ", familyMember='" + familyMember + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
