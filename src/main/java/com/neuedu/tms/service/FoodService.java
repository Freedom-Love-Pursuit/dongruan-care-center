package com.neuedu.tms.service;

import com.neuedu.tms.dao.FoodDao;
import com.neuedu.tms.pojo.Food;

import java.util.List;

/**
 * 菜品管理服务
 */
public class FoodService {

    private FoodDao foodDao = new FoodDao();

    /**
     * 查询所有菜品。
     * 实现：调用foodDao.listAll查询全部菜品。
     */
    public List<Food> listAll() {
        try {
            return foodDao.listAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询菜品。
     * 实现：调用foodDao.getById按ID查询并返回。
     */
    public Food getById(int id) {
        try {
            return foodDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增菜品，校验名称唯一性后写入。
     * 实现：先调foodDao.existsByName检查重复，无重复则调foodDao.add写入。
     */
    public boolean addFood(Food food) {
        try {
            if (foodDao.existsByName(food.getFoodName())) {
                System.out.println("菜品名称 '" + food.getFoodName() + "' 已存在，新增失败！");
                return false;
            }
            foodDao.add(food);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新菜品信息。
     * 实现：调用foodDao.update更新菜品记录。
     */
    public boolean updateFood(Food food) {
        try {
            foodDao.update(food);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除菜品。
     * 实现：调用foodDao.delete删除菜品记录。
     */
    public boolean deleteFood(int id) {
        try {
            foodDao.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据菜品名称模糊搜索。
     * 实现：调用foodDao.searchByName按关键字模糊匹配并返回。
     */
    public List<Food> searchByName(String keyword) {
        try {
            return foodDao.searchByName(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
