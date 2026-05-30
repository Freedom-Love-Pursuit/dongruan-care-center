package com.neuedu.tms.view;

import com.neuedu.tms.pojo.User;
import com.neuedu.tms.utils.InputUtil;

/**
 * 健康管家主菜单视图
 * 提供健康管家功能入口，通过命令模式分发到各子视图
 */
public class StewardMainView extends BaseView {

    /**
     * 显示健康管家主菜单
     * @param currentUser 当前登录的健康管家用户
     */
    public void show(User currentUser) {
        while (true) {
            printHeader("健康管家主菜单 - 当前用户: " + currentUser.getNickname());
            System.out.println("  1. 我的客户");
            System.out.println("  2. 日常护理");
            System.out.println("  3. 护理记录查询");
            System.out.println("  4. 外出申请");
            System.out.println("  5. 退住申请");
            System.out.println("  0. 退出登录");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            switch (choice) {
                case 1:
                    new StewardCustomerView(currentUser).execute();
                    break;
                case 2:
                    new DailyNurseView(currentUser).execute();
                    break;
                case 3:
                    new NurseRecordQueryView(currentUser).execute();
                    break;
                case 4:
                    new OutwardApplyView(currentUser).execute();
                    break;
                case 5:
                    new BackdownApplyView(currentUser).execute();
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
