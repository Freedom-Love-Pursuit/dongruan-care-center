package com.neuedu.tms.pojo;

/**
 * 护理等级实体类，定义护理等级分类。
 * 字段：等级名称、等级状态(启用/停用)等。
 */
public class NurseLevel {

    private int id;
    private String levelName;
    private int levelStatus;   // 1-启用, 2-停用
    private int isDeleted;     // 0-正常, 1-已删除

    public NurseLevel() {
    }

    public NurseLevel(int id, String levelName, int levelStatus, int isDeleted) {
        this.id = id;
        this.levelName = levelName;
        this.levelStatus = levelStatus;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(int levelStatus) {
        this.levelStatus = levelStatus;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "NurseLevel{" +
                "id=" + id +
                ", levelName='" + levelName + '\'' +
                ", levelStatus=" + levelStatus +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
