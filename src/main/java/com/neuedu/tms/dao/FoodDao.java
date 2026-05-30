package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Food;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodDao {

    private static final String DATA_FILE = "food.json";

    /**
     * 查询所有未删除的菜品列表。
     * 实现：读取food.json，过滤isDeleted==0后返回。
     */
    public List<Food> listAll() {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return new ArrayList<>();
            }
            return foods.stream()
                    .filter(f -> f.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询未删除的菜品。
     * 实现：遍历food.json，匹配id且isDeleted==0后返回。
     */
    public Food getById(int id) {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return null;
            }
            for (Food f : foods) {
                if (f.getId() == id && f.getIsDeleted() == 0) {
                    return f;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增菜品，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回food.json。
     */
    public void add(Food food) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            food.setId(nextId);
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                foods = new ArrayList<>();
            }
            foods.add(food);
            JsonUtil.writeList(DATA_FILE, foods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新菜品信息。
     * 实现：遍历列表匹配id，替换后写回food.json。
     */
    public void update(Food food) {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return;
            }
            for (int i = 0; i < foods.size(); i++) {
                if (foods.get(i).getId() == food.getId()) {
                    foods.set(i, food);
                    JsonUtil.writeList(DATA_FILE, foods);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除菜品。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回food.json。
     */
    public void delete(int id) {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return;
            }
            for (Food f : foods) {
                if (f.getId() == id) {
                    f.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, foods);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按菜品名称模糊搜索。
     * 实现：过滤food.json中isDeleted==0且foodName包含关键词后返回。
     */
    public List<Food> searchByName(String keyword) {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return new ArrayList<>();
            }
            String lowerKeyword = keyword.toLowerCase();
            return foods.stream()
                    .filter(f -> f.getIsDeleted() == 0
                            && f.getFoodName() != null
                            && f.getFoodName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 校验菜品名称是否已存在（排除已删除记录）。
     * 实现：遍历food.json，匹配foodName且isDeleted==0后返回布尔。
     */
    public boolean existsByName(String foodName) {
        try {
            List<Food> foods = JsonUtil.readList(DATA_FILE, Food.class);
            if (foods == null) {
                return false;
            }
            for (Food f : foods) {
                if (f.getIsDeleted() == 0 && foodName.equals(f.getFoodName())) {
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
