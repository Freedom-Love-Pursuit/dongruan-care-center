package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.UserService;
import com.neuedu.tms.utils.InputUtil;

/**
 * 登录视图 - 处理用户登录逻辑
 * 最多允许5次登录尝试，输入0可主动退出
 */
public class LoginView extends BaseView {

    private User currentUser;
    private UserService userService;

    public LoginView() {
        this.userService = new UserService();
    }

    /**
     * 获取当前登录用户
     * @return 已登录的 User 对象，未登录时为 null
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 显示登录界面，循环直到登录成功、主动退出或达到最大尝试次数
     * @return true 登录成功, false 登录失败（主动退出或超过最大尝试次数）
     */
    public boolean showLogin() {
        printHeader("用户登录");
        System.out.println("（输入 0 可退出系统）");
        System.out.println();

        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("(第 " + attempt + " / " + maxAttempts + " 次尝试)");

            String username = InputUtil.readString("请输入用户名（输入0退出）: ");

            // 输入 0 主动退出
            if ("0".equals(username.trim())) {
                System.out.println("用户主动退出，再见！");
                return false;
            }

            String password = InputUtil.readString("请输入密码: ");

            // 通过 UserService 验证
            try {
                User user = userService.login(username, password);
                if (user != null) {
                    this.currentUser = user;
                    String roleName = RoleEnum.fromId(user.getRoleId()).getRoleName();
                    System.out.println("\n登录成功！欢迎您，" + user.getNickname() + "（" + roleName + "）");
                    return true;
                }
            } catch (Exception e) {
                // 验证异常，继续下一轮尝试
                System.out.println("登录失败：" + e.getMessage());
            }

            // 登录失败提示
            if (attempt < maxAttempts) {
                System.out.println("用户名或密码错误，请重新输入！还剩 " + (maxAttempts - attempt) + " 次机会。\n");
            } else {
                System.out.println("用户名或密码错误，尝试次数已用完。");
            }
        }
        return false;
    }
}
