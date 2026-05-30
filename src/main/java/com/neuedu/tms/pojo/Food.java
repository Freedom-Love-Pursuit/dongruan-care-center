package com.neuedu.tms.pojo;

/**
 * 食品实体类，记录膳食系统中的食品信息。
 * 字段：食品名称、食品类型(主食/菜品/汤品/水果/饮品)等。
 */
public class Food {

    private int id;
    private String foodName;      // 食品名称
    private String foodType;      // 食品类型（主食/菜品/汤品/水果/饮品）
    private int isDeleted;        // 逻辑删除 0正常/1已删除

    public Food() {
    }

    public Food(int id, String foodName, String foodType, int isDeleted) {
        this.id = id;
        this.foodName = foodName;
        this.foodType = foodType;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", foodName='" + foodName + '\'' +
                ", foodType='" + foodType + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
