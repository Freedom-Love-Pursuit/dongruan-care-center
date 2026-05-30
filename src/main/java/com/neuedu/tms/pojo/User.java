package com.neuedu.tms.pojo;

/**
 * 用户实体类，记录系统用户的基本信息。
 * 字段：昵称、用户名、密码、性别、手机号、角色(管理员/健康管家)、创建时间等。
 */
public class User {

    private int id;
    private String nickname;
    private String username;
    private String password;
    private int sex;           // 0-女, 1-男
    private String phoneNumber;
    private int roleId;        // 1-管理员, 2-健康管家
    private int isDeleted;     // 0-正常, 1-已删除
    private String createTime; // yyyy-MM-dd

    public User() {
    }

    public User(int id, String nickname, String username, String password, int sex,
                String phoneNumber, int roleId, int isDeleted, String createTime) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
        this.isDeleted = isDeleted;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roleId=" + roleId +
                ", isDeleted=" + isDeleted +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
