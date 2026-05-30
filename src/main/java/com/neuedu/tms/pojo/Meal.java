package com.neuedu.tms.pojo;

/**
 * 餐食安排实体类，记录每周每日各餐次的食品安排。
 * 字段：星期、餐次(早餐/午餐/晚餐)、关联食品ID等。
 */
public class Meal {

    private int id;
    private int weekDay;          // 星期几 1-7
    private int mealType;         // 餐次 1早餐/2午餐/3晚餐
    private int foodId;           // 关联食品ID
    private int isDeleted;        // 逻辑删除 0正常/1已删除

    public Meal() {
    }

    public Meal(int id, int weekDay, int mealType, int foodId, int isDeleted) {
        this.id = id;
        this.weekDay = weekDay;
        this.mealType = mealType;
        this.foodId = foodId;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", weekDay=" + weekDay +
                ", mealType=" + mealType +
                ", foodId=" + foodId +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
