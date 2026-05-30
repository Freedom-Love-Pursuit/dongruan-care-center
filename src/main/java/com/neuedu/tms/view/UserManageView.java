package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.UserService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 用户管理视图 - 管理员管理用户（增删改查）
 */
public class UserManageView extends BaseView implements MenuItem {

    private UserService userService = new UserService();

    @Override
    public void execute() {
        while (true) {
            printHeader("用户管理");
            System.out.println("  1. 用户列表");
            System.out.println("  2. 添加用户");
            System.out.println("  3. 修改用户信息");
            System.out.println("  4. 删除用户");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listUsers(); break;
                    case 2: addUser(); break;
                    case 3: updateUser(); break;
                    case 4: deleteUser(); break;
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

    /**
     * 列出所有用户
     */
    private void listUsers() {
        printHeader("用户列表");
        List<User> list = userService.listAll();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-12s %-10s %-5s %-15s %-10s %-12s\n",
                    "ID", "昵称", "用户名", "性别", "电话", "角色", "创建时间");
            printDivider();
            for (User u : list) {
                String sexText = Gender.textOf(u.getSex());
                String roleText = RoleEnum.fromId(u.getRoleId()).getRoleName();
                System.out.printf("%-5d %-12s %-10s %-5s %-15s %-10s %-12s\n",
                        u.getId(), u.getNickname(), u.getUsername(), sexText,
                        u.getPhoneNumber() != null ? u.getPhoneNumber() : "-",
                        roleText,
                        u.getCreateTime() != null ? u.getCreateTime() : "-");
            }
        }
        waitEnter();
    }

    /**
     * 添加用户
     */
    private void addUser() {
        printHeader("添加用户");
        User user = new User();

        user.setNickname(InputUtil.readString("昵称: "));
        user.setUsername(InputUtil.readString("用户名: "));
        user.setSex(InputUtil.readInt("性别(0-女, 1-男): ", 0, 1));
        user.setPhoneNumber(InputUtil.readString("电话号码: "));
        user.setRoleId(InputUtil.readInt("角色(1-管理员, 2-健康管家): ", 1, 2));

        // 密码自动取手机号后6位
        String phone = user.getPhoneNumber();
        if (phone != null && phone.length() >= 6) {
            user.setPassword(phone.substring(phone.length() - 6));
        } else {
            user.setPassword("123456"); // 默认密码
        }
        System.out.println("自动生成密码（手机号后6位）: " + user.getPassword());

        boolean success = userService.addUser(user);
        if (success) {
            System.out.println("\n用户添加成功！");
        } else {
            System.out.println("\n用户添加失败，用户名可能已存在！");
        }
        waitEnter();
    }

    /**
     * 修改用户信息
     */
    private void updateUser() {
        printHeader("修改用户信息");
        int id = InputUtil.readInt("请输入要修改的用户ID (0返回): ");
        if (id == 0) return;
        User user = userService.getById(id);
        if (user == null) {
            System.out.println("用户不存在！");
            waitEnter();
            return;
        }

        System.out.println("当前信息: " + user.getNickname() + " (" + user.getUsername() + ")");
        System.out.println("（直接回车保留原值）");

        String nickname = InputUtil.readString("昵称 [" + user.getNickname() + "]: ", user.getNickname());
        user.setNickname(nickname);

        String username = InputUtil.readString("用户名 [" + user.getUsername() + "]: ", user.getUsername());
        user.setUsername(username);

        String password = InputUtil.readString("密码（直接回车不修改）: ", "");
        if (!password.isEmpty()) {
            user.setPassword(password);
        }

        String sexStr = InputUtil.readString("性别(0-女, 1-男) [" + user.getSex() + "]: ", String.valueOf(user.getSex()));
        user.setSex(Integer.parseInt(sexStr));

        String phone = InputUtil.readString("电话 [" + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "") + "]: ", user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        user.setPhoneNumber(phone);

        String roleStr = InputUtil.readString("角色(1-管理员, 2-健康管家) [" + user.getRoleId() + "]: ", String.valueOf(user.getRoleId()));
        user.setRoleId(Integer.parseInt(roleStr));

        userService.updateUser(user);
        System.out.println("\n用户信息修改成功！");
        waitEnter();
    }

    /**
     * 删除用户（逻辑删除）
     */
    private void deleteUser() {
        printHeader("删除用户");
        int delId = InputUtil.readInt("请输入要删除的用户ID (0返回): ");
        if (delId == 0) return;
        User delUser = userService.getById(delId);
        if (delUser == null) {
            System.out.println("用户不存在！");
            waitEnter();
            return;
        }

        String roleText = RoleEnum.fromId(delUser.getRoleId()).getRoleName();
        System.out.println("用户: " + delUser.getNickname() + " (" + delUser.getUsername() + ") - " + roleText);

        boolean confirm = InputUtil.readConfirm("确认删除？");
        if (confirm) {
            userService.deleteUser(delId);
            System.out.println("用户已删除。");
        } else {
            System.out.println("已取消删除操作。");
        }
        waitEnter();
    }
}
