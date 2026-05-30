package com.neuedu.tms.view;

import com.neuedu.tms.pojo.User;
import com.neuedu.tms.utils.InputUtil;

/**
 * 管理员主菜单视图
 * 提供系统管理功能的入口，通过命令模式分发到各子视图
 */
public class AdminMainView extends BaseView {

    /**
     * 显示管理员主菜单
     * @param currentUser 当前登录的管理员用户
     */
    public void show(User currentUser) {
        while (true) {
            printHeader("管理员主菜单 - 当前用户: " + currentUser.getNickname());
            System.out.println("  1. 客户管理");
            System.out.println("  2. 床位管理");
            System.out.println("  3. 护理管理");
            System.out.println("  4. 外出登记管理");
            System.out.println("  5. 退住登记管理");
            System.out.println("  6. 用户管理");
            System.out.println("  7. 膳食菜品管理");
            System.out.println("  8. 膳食日历编排");
            System.out.println("  0. 退出登录");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            switch (choice) {
                case 1:
                    new CustomerManageView().execute();
                    break;
                case 2:
                    new BedManageView().execute();
                    break;
                case 3:
                    new NurseManageView().execute();
                    break;
                case 4:
                    new OutwardManageView().execute();
                    break;
                case 5:
                    new BackdownManageView().execute();
                    break;
                case 6:
                    new UserManageView().execute();
                    break;
                case 7:
                    new FoodManageView().execute();
                    break;
                case 8:
                    new MealCalendarView().execute();
                    break;
                case 0:
                    System.out.println("\n已退出登录。");
                    return;
                default:
                    System.out.println("无效选项，请重新选择！");
                    waitEnter();
            }
        }
    }
}
