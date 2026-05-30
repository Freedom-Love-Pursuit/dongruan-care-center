package com.neuedu.tms.pojo;

/**
 * 护理等级与护理项目关联实体类，记录每个等级包含的护理项目。
 * 字段：等级ID、护理项目ID。
 */
public class NurseLevelItem {

    private int id;
    private int levelId;
    private int itemId;

    public NurseLevelItem() {
    }

    public NurseLevelItem(int id, int levelId, int itemId) {
        this.id = id;
        this.levelId = levelId;
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "NurseLevelItem{" +
                "id=" + id +
                ", levelId=" + levelId +
                ", itemId=" + itemId +
                '}';
    }
}
