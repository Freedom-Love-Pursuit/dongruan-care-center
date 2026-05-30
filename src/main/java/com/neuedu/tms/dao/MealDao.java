package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Meal;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MealDao {

    private static final String DATA_FILE = "meal.json";

    /**
     * 查询所有未删除的餐食列表。
     * 实现：读取meal.json，过滤isDeleted==0后返回。
     */
    public List<Meal> listAll() {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return new ArrayList<>();
            }
            return meals.stream()
                    .filter(m -> m.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按星期查询未删除的餐食列表。
     * 实现：过滤meal.json中weekDay匹配且isDeleted==0的记录。
     */
    public List<Meal> listByWeekDay(int weekDay) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return new ArrayList<>();
            }
            return meals.stream()
                    .filter(m -> m.getIsDeleted() == 0 && m.getWeekDay() == weekDay)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询未删除的餐食记录。
     * 实现：遍历meal.json，匹配id且isDeleted==0后返回。
     */
    public Meal getById(int id) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return null;
            }
            for (Meal m : meals) {
                if (m.getId() == id && m.getIsDeleted() == 0) {
                    return m;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增餐食记录，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回meal.json。
     */
    public void add(Meal meal) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            meal.setId(nextId);
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                meals = new ArrayList<>();
            }
            meals.add(meal);
            JsonUtil.writeList(DATA_FILE, meals);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新餐食记录。
     * 实现：遍历列表匹配id，替换后写回meal.json。
     */
    public void update(Meal meal) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return;
            }
            for (int i = 0; i < meals.size(); i++) {
                if (meals.get(i).getId() == meal.getId()) {
                    meals.set(i, meal);
                    JsonUtil.writeList(DATA_FILE, meals);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除餐食记录。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回meal.json。
     */
    public void delete(int id) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return;
            }
            for (Meal m : meals) {
                if (m.getId() == id) {
                    m.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, meals);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按星期和餐次查询未删除的餐食列表。
     * 实现：过滤meal.json中weekDay+mealType匹配且isDeleted==0的记录。
     */
    public List<Meal> listByWeekDayAndType(int weekDay, int mealType) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return new ArrayList<>();
            }
            return meals.stream()
                    .filter(m -> m.getIsDeleted() == 0
                            && m.getWeekDay() == weekDay
                            && m.getMealType() == mealType)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按星期、餐次和菜品检查是否已存在。
     * 实现：遍历meal.json，匹配weekDay+mealType+foodId且isDeleted==0后返回布尔。
     */
    public boolean existsByWeekDayAndTypeAndFood(int weekDay, int mealType, int foodId) {
        try {
            List<Meal> meals = JsonUtil.readList(DATA_FILE, Meal.class);
            if (meals == null) {
                return false;
            }
            for (Meal m : meals) {
                if (m.getIsDeleted() == 0
                        && m.getWeekDay() == weekDay
                        && m.getMealType() == mealType
                        && m.getFoodId() == foodId) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
