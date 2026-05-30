package com.neuedu.tms.service;

import com.neuedu.tms.dao.MealDao;
import com.neuedu.tms.dao.FoodDao;
import com.neuedu.tms.pojo.Meal;
import com.neuedu.tms.pojo.Food;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 膳食计划管理服务
 */
public class MealService {

    private MealDao mealDao = new MealDao();
    private FoodDao foodDao = new FoodDao();

    /**
     * 根据星期几查询膳食计划。
     * 实现：调用mealDao.listByWeekDay按星期几查询并返回。
     */
    public List<Meal> listByWeekDay(int weekDay) {
        try {
            return mealDao.listByWeekDay(weekDay);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据星期几和餐次类型查询膳食计划。
     * 实现：调用mealDao.listByWeekDayAndType按星期和餐次类型查询并返回。
     */
    public List<Meal> listByWeekDayAndType(int weekDay, int mealType) {
        try {
            return mealDao.listByWeekDayAndType(weekDay, mealType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增膳食计划，重名检测（同日+同餐次+同菜品视为重复）。
     * 实现：先调mealDao.existsByWeekDayAndTypeAndFood检查重复，无重复则调add写入。
     */
    public boolean addMeal(Meal meal) {
        try {
            if (mealDao.existsByWeekDayAndTypeAndFood(meal.getWeekDay(), meal.getMealType(), meal.getFoodId())) {
                System.out.println("该膳食计划已存在，新增失败！");
                return false;
            }
            mealDao.add(meal);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新膳食计划。
     * 实现：调用mealDao.update更新膳食计划记录。
     */
    public boolean updateMeal(Meal meal) {
        try {
            mealDao.update(meal);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除膳食计划。
     * 实现：调用mealDao.delete删除膳食计划记录。
     */
    public boolean deleteMeal(int id) {
        try {
            mealDao.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一周菜单汇总，返回嵌套Map：weekDay->mealType->菜品名称列表。
     * 实现：遍历1-7天和1-3餐次，逐项调mealDao.listByWeekDayAndType和foodDao.getById拼装结果。
     */
    public Map<Integer, Map<Integer, List<String>>> getWeekMenu() {
        Map<Integer, Map<Integer, List<String>>> weekMenu = new LinkedHashMap<>();
        try {
            for (int day = 1; day <= 7; day++) {
                Map<Integer, List<String>> dayMenu = new LinkedHashMap<>();
                for (int type = 1; type <= 3; type++) {
                    List<Meal> meals = mealDao.listByWeekDayAndType(day, type);
                    List<String> foodNames = new ArrayList<>();
                    if (meals != null) {
                        for (Meal meal : meals) {
                            Food food = foodDao.getById(meal.getFoodId());
                            if (food != null) {
                                foodNames.add(food.getFoodName());
                            }
                        }
                    }
                    dayMenu.put(type, foodNames);
                }
                weekMenu.put(day, dayMenu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weekMenu;
    }
}
