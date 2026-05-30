package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 健康管家 - 我的客户视图
 * 查看分配给当前管家的客户列表及详细信息
 */
public class StewardCustomerView extends BaseView implements MenuItem {

    private User currentUser;
    private CustomerService customerService = new CustomerService();

    /**
     * 构造健康管家客户视图。
     * @param currentUser 当前登录的健康管家用户
     */
    public StewardCustomerView(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 进入我的客户菜单循环。
     * 实现：死循环列出当前管家所负责的客户列表，按序号查看详情；客户为空时直接返回。
     */
    @Override
    public void execute() {
        while (true) {
            printHeader("我的客户");
            List<Customer> list = customerService.listBySteward(currentUser.getId());
            if (list == null || list.isEmpty()) {
                System.out.println("您当前没有分配的客户。");
            } else {
                System.out.printf("%-5s %-10s %-3s %-5s %-8s %-12s %-12s\n",
                        "序号", "姓名", "年龄", "性别", "房间号", "入住日期", "联系电话");
                printDivider();
                for (int i = 0; i < list.size(); i++) {
                    Customer c = list.get(i);
                    String sexText = Gender.textOf(c.getCustomerSex());
                    System.out.printf("%-5d %-10s %-3d %-5s %-8s %-12s %-12s\n",
                            (i + 1), c.getCustomerName(), c.getCustomerAge(), sexText,
                            c.getRoomNo() != null ? c.getRoomNo() : "-",
                            c.getCheckinDate() != null ? c.getCheckinDate() : "-",
                            c.getContactTel() != null ? c.getContactTel() : "-");
                }

                System.out.println("\n  输入客户序号查看详情，或输入 0 返回");
                int choice = InputUtil.readInt("请选择: ");

                if (choice == 0) {
                    return;
                }
                if (choice >= 1 && choice <= list.size()) {
                    showCustomerDetail(list.get(choice - 1));
                } else {
                    System.out.println("无效选项！");
                    waitEnter();
                }
            }

            if (list == null || list.isEmpty()) {
                waitEnter();
                return;
            }
        }
    }

    /**
     * 显示客户详细信息
     */
    private void showCustomerDetail(Customer c) {
        printHeader("客户详情: " + c.getCustomerName());
        printDivider();
        System.out.println("  客户ID: " + c.getId());
        System.out.println("  姓名: " + c.getCustomerName());
        System.out.println("  年龄: " + c.getCustomerAge());
        System.out.println("  性别: " + Gender.textOf(c.getCustomerSex()));
        System.out.println("  身份证号: " + (c.getIdcard() != null ? c.getIdcard() : "-"));
        System.out.println("  楼号: " + (c.getBuildingNo() != null ? c.getBuildingNo() : "-"));
        System.out.println("  房间号: " + (c.getRoomNo() != null ? c.getRoomNo() : "-"));
        System.out.println("  入住日期: " + (c.getCheckinDate() != null ? c.getCheckinDate() : "-"));
        System.out.println("  联系电话: " + (c.getContactTel() != null ? c.getContactTel() : "-"));
        System.out.println("  出生日期: " + (c.getBirthday() != null ? c.getBirthday() : "-"));
        System.out.println("  身高: " + (c.getHeight() != null ? c.getHeight() : "-"));
        System.out.println("  体重: " + (c.getWeight() != null ? c.getWeight() : "-"));
        System.out.println("  血型: " + (c.getBloodType() != null ? c.getBloodType() : "-"));
        System.out.println("  家属: " + (c.getFamilyMember() != null ? c.getFamilyMember() : "-"));
        System.out.println("  身心状况: " + (c.getPsychosomaticState() != null && !c.getPsychosomaticState().isEmpty() ? c.getPsychosomaticState() : "无"));
        System.out.println("  护理等级ID: " + (c.getLevelId() != null ? c.getLevelId() : "未设置"));
        waitEnter();
    }
}
