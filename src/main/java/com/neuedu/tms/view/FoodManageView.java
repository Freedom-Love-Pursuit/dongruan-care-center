package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Food;
import com.neuedu.tms.service.FoodService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 膳食管理视图 - 管理员管理菜品信息（增删改查）
 */
public class FoodManageView extends BaseView implements MenuItem {

    private FoodService foodService = new FoodService();

    /**
     * 进入膳食管理菜单循环。
     * 实现：死循环显示菜品管理的增删改查搜索菜单，根据用户选择调用对应private方法，选0退出。
     */
    @Override
    public void execute() {
        while (true) {
            printHeader("膳食管理 - 菜品库");
            System.out.println("  1. 菜品列表");
            System.out.println("  2. 添加菜品");
            System.out.println("  3. 修改菜品");
            System.out.println("  4. 删除菜品");
            System.out.println("  5. 搜索菜品");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");
            try {
                switch (choice) {
                    case 1: listFoods(); break;
                    case 2: addFood(); break;
                    case 3: updateFood(); break;
                    case 4: deleteFood(); break;
                    case 5: searchFood(); break;
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

    /// 列出全部菜品。实现：调foodService.listAll()获取菜品列表，调用showFoodList表格输出。
    private void listFoods() {
        printHeader("菜品列表");
        List<Food> list = foodService.listAll();
        showFoodList(list);
        waitEnter();
    }

    /// 添加菜品。实现：接收菜品名称和类型输入，构造Food对象调foodService.addFood写入数据库。
    private void addFood() {
        printHeader("添加菜品");
        Food food = new Food();
        food.setFoodName(InputUtil.readString("菜品名称: "));
        food.setFoodType(InputUtil.readString("菜品类型(主食/菜品/汤品/水果/饮品): "));
        food.setIsDeleted(0);

        boolean ok = foodService.addFood(food);
        System.out.println(ok ? "\n菜品添加成功！" : "\n菜品添加失败，名称可能已存在！");
        waitEnter();
    }

    /// 修改菜品信息。实现：根据ID查菜品，显示当前值，按回车保留原值、输入新值更新后调foodService.updateFood保存。
    private void updateFood() {
        printHeader("修改菜品");
        int id = InputUtil.readInt("请输入菜品ID (0返回): ");
        if (id == 0) return;
        Food food = foodService.getById(id);
        if (food == null) { System.out.println("菜品不存在！"); waitEnter(); return; }

        System.out.println("当前: " + food.getFoodName() + " - " + food.getFoodType());
        System.out.println("（直接回车保留原值）");

        String name = InputUtil.readString("名称 [" + food.getFoodName() + "]: ", food.getFoodName());
        food.setFoodName(name);

        String type = InputUtil.readString("类型 [" + food.getFoodType() + "]: ", food.getFoodType());
        food.setFoodType(type);

        foodService.updateFood(food);
        System.out.println("\n菜品修改成功！");
        waitEnter();
    }

    /// 删除菜品。实现：根据ID查菜品并确认，调foodService.deleteFood执行逻辑删除。
    private void deleteFood() {
        printHeader("删除菜品");
        int id = InputUtil.readInt("请输入菜品ID (0返回): ");
        if (id == 0) return;
        Food food = foodService.getById(id);
        if (food == null) { System.out.println("菜品不存在！"); waitEnter(); return; }

        System.out.println("菜品: " + food.getFoodName() + " - " + food.getFoodType());
        if (InputUtil.readConfirm("确认删除？")) {
            foodService.deleteFood(id);
            System.out.println("菜品已删除。");
        } else {
            System.out.println("已取消。");
        }
        waitEnter();
    }

    /// 模糊搜索菜品。实现：接收关键字，调foodService.searchByName模糊匹配，调用showFoodList输出结果。
    private void searchFood() {
        printHeader("搜索菜品");
        String keyword = InputUtil.readString("请输入菜品名称关键字: ");
        List<Food> list = foodService.searchByName(keyword);
        System.out.println("找到 " + (list != null ? list.size() : 0) + " 个匹配:");
        showFoodList(list);
        waitEnter();
    }

    /// 表格形式输出菜品列表。实现：判空后按固定列宽格式化输出ID、名称、类型三列。
    private void showFoodList(List<Food> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
            return;
        }
        System.out.printf("%-5s %-15s %-10s\n", "ID", "菜品名称", "菜品类型");
        printDivider();
        for (Food f : list) {
            System.out.printf("%-5d %-15s %-10s\n",
                    f.getId(), f.getFoodName(), f.getFoodType());
        }
    }
}
