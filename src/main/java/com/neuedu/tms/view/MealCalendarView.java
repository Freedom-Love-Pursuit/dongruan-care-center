package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Meal;
import com.neuedu.tms.service.MealService;
import com.neuedu.tms.service.FoodService;
import com.neuedu.tms.pojo.Food;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 膳食日历视图 - 管理员编排每日三餐菜单
 */
public class MealCalendarView extends BaseView implements MenuItem {

    private MealService mealService = new MealService();
    private FoodService foodService = new FoodService();

    /**
     * 进入膳食日历管理菜单循环。
     * 实现：死循环显示菜单，支持查看一周/某天菜单、添加/删除膳食安排，选0退出。
     */
    @Override
    public void execute() {
        while (true) {
            printHeader("膳食日历管理");
            System.out.println("  1. 查看一周菜单");
            System.out.println("  2. 查看某天菜单");
            System.out.println("  3. 添加膳食安排");
            System.out.println("  4. 删除膳食安排");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");
            try {
                switch (choice) {
                    case 1: showWeekMenu(); break;
                    case 2: showDayMenu(); break;
                    case 3: addMeal(); break;
                    case 4: deleteMeal(); break;
                    case 0: return;
                    default:
                        System.out.println("无效选项，请重新选择！");
                        waitEnter();
                }
            } catch (Exception e) {
                System.out.println("操作失败: " + e.getMessage());
                waitEnter();
            }
        }
    }

    /// 展示七天完整菜单。实现：遍历周一至周日，调用showMealsForDay输出每天三餐。
    private void showWeekMenu() {
        printHeader("一周膳食菜单");
        String[] dayNames = {"", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

        for (int day = 1; day <= 7; day++) {
            System.out.println("\n--- " + dayNames[day] + " ---");
            showMealsForDay(day, true);
        }
        waitEnter();
    }

    /// 展示某天菜单。实现：接收用户输入的星期几(1-7)，调用showMealsForDay输出三餐。
    private void showDayMenu() {
        printHeader("查看某天菜单");
        int day = InputUtil.readInt("请输入星期几(1-7): ", 1, 7);
        showMealsForDay(day, false);
        waitEnter();
    }

    /// 显示某天的三餐。实现：遍历早中晚三餐(1/2/3)，查mealService获取每餐菜品列表，关联foodService获取菜品名称后拼接显示。
    private void showMealsForDay(int weekDay, boolean compact) {
        String[] mealTypeNames = {"", "早餐", "午餐", "晚餐"};
        for (int mt = 1; mt <= 3; mt++) {
            List<Meal> meals = mealService.listByWeekDayAndType(weekDay, mt);
            if (meals == null || meals.isEmpty()) {
                if (!compact) System.out.println("  " + mealTypeNames[mt] + ": 暂无安排");
                continue;
            }
            System.out.print("  " + mealTypeNames[mt] + ": ");
            for (int i = 0; i < meals.size(); i++) {
                Food food = foodService.getById(meals.get(i).getFoodId());
                if (food != null) {
                    System.out.print(food.getFoodName());
                    if (i < meals.size() - 1) System.out.print("、");
                }
            }
            System.out.println();
        }
    }

    /// 添加膳食安排。实现：输入星期几和餐次，显示菜品库供选择，用选中菜品ID构造Meal对象调mealService.addMeal写入。
    private void addMeal() {
        printHeader("添加膳食安排");
        int weekDay = InputUtil.readInt("星期几(1-7): ", 1, 7);
        int mealType = InputUtil.readInt("餐次(1-早餐, 2-午餐, 3-晚餐): ", 1, 3);

        // 显示现有菜品库
        System.out.println("\n--- 菜品库 ---");
        List<Food> foods = foodService.listAll();
        if (foods == null || foods.isEmpty()) {
            System.out.println("暂无菜品，请先添加菜品！");
            waitEnter();
            return;
        }
        for (Food f : foods) {
            System.out.printf("  ID: %d  %s (%s)\n", f.getId(), f.getFoodName(), f.getFoodType());
        }

        int foodId = InputUtil.readInt("\n请选择菜品ID (0取消): ");
        if (foodId == 0) return;
        Meal meal = new Meal();
        meal.setWeekDay(weekDay);
        meal.setMealType(mealType);
        meal.setFoodId(foodId);
        meal.setIsDeleted(0);

        boolean ok = mealService.addMeal(meal);
        System.out.println(ok ? "\n膳食安排添加成功！" : "\n添加失败，该天该餐次已有此菜品！");
        waitEnter();
    }

    /// 删除膳食安排。实现：输入星期几，显示该天所有膳食安排列表，选记录ID确认后调mealService.deleteMeal删除。
    private void deleteMeal() {
        printHeader("删除膳食安排");
        int weekDay = InputUtil.readInt("星期几(1-7): ", 1, 7);

        // 显示该天所有膳食安排
        System.out.println("\n当前安排:");
        List<Meal> all = mealService.listByWeekDay(weekDay);
        if (all == null || all.isEmpty()) {
            System.out.println("该天暂无膳食安排。");
            waitEnter();
            return;
        }
        System.out.printf("%-5s %-6s %-5s %-15s\n", "ID", "餐次", "菜品ID", "菜品名称");
        printDivider();
        String[] mtNames = {"", "早餐", "午餐", "晚餐"};
        for (Meal m : all) {
            Food f = foodService.getById(m.getFoodId());
            System.out.printf("%-5d %-6s %-5d %-15s\n",
                    m.getId(), mtNames[m.getMealType()], m.getFoodId(),
                    f != null ? f.getFoodName() : "未知");
        }

        int id = InputUtil.readInt("\n请输入要删除的记录ID (0取消): ");
        if (id == 0) return;
        if (InputUtil.readConfirm("确认删除？")) {
            mealService.deleteMeal(id);
            System.out.println("已删除。");
        } else {
            System.out.println("已取消。");
        }
        waitEnter();
    }
}
