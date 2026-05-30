package com.neuedu.tms;

import com.neuedu.tms.enums.RoleEnum;

/**
 * 东软颐养中心管理系统 - 程序入口
 * 使用命令模式架构的视图层
 */
public class Main {
    /**
     * 程序入口，启动东软颐养中心管理系统。
     * 首先展示登录界面，登录成功后根据用户角色(管理员/健康管家)跳转到对应主界面。
     * 登录失败或用户选择退出时结束程序。
     */
    public static void main(String[] args) {
        com.neuedu.tms.view.LoginView loginView = new com.neuedu.tms.view.LoginView();
        while (true) {
            boolean loggedIn = loginView.showLogin();
            if (!loggedIn) {
                System.out.println("程序退出，再见！");
                return;
            }
            com.neuedu.tms.pojo.User currentUser = loginView.getCurrentUser();
            if (currentUser.getRoleId() == RoleEnum.ADMIN.getRoleId()) {
                new com.neuedu.tms.view.AdminMainView().show(currentUser);
            } else {
                new com.neuedu.tms.view.StewardMainView().show(currentUser);
            }
        }
    }
}
