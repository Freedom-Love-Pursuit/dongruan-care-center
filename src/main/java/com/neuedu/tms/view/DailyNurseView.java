package com.neuedu.tms.view;

import com.neuedu.tms.pojo.*;
import com.neuedu.tms.service.*;
import com.neuedu.tms.utils.InputUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 健康管家 - 日常护理视图
 * 选择客户 → 显示护理项目 → 选择项目 → 填写记录 → 提交
 */
public class DailyNurseView extends BaseView implements MenuItem {

    private User currentUser;
    private CustomerService customerService = new CustomerService();
    private NurseService nurseService = new NurseService();
    private NurseRecordService nurseRecordService = new NurseRecordService();
    private CustomerNurseItemService customerNurseItemService = new CustomerNurseItemService();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 构造日常护理视图。
     * @param currentUser 当前登录的健康管家用户
     */
    public DailyNurseView(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 进入日常护理菜单循环。
     * 实现：死循环中先选客户，再选护理项目填写记录并提交；选客户返回则退出。
     */
    @Override
    public void execute() {
        while (true) {
            // 第一步：选择客户
            Customer customer = selectCustomer();
            if (customer == null) {
                return; // 用户选择返回
            }

            // 第二步：选择护理项目并记录
            performNursing(customer);
        }
    }

    /// 选择客户。实现：列出管家负责的客户列表，按序号选择，选0返回null。
    private Customer selectCustomer() {
        printHeader("日常护理 - 选择客户");
        List<Customer> list = customerService.listBySteward(currentUser.getId());
        if (list == null || list.isEmpty()) {
            System.out.println("您当前没有分配的客户。");
            waitEnter();
            return null;
        }

        for (int i = 0; i < list.size(); i++) {
            Customer c = list.get(i);
            System.out.printf("  %d. %s (房间: %s, 年龄: %d)\n",
                    (i + 1), c.getCustomerName(),
                    c.getRoomNo() != null ? c.getRoomNo() : "-",
                    c.getCustomerAge());
        }
        System.out.println("  0. 返回");

        int choice = InputUtil.readInt("请选择客户: ");
        if (choice == 0) {
            return null;
        }
        if (choice >= 1 && choice <= list.size()) {
            return list.get(choice - 1);
        }
        System.out.println("无效选项！");
        waitEnter();
        return null;
    }

    /**
     * 执行护理操作
     */
    private void performNursing(Customer customer) {
        printHeader("日常护理 - " + customer.getCustomerName());

        // 获取客户的护理项目
        List<NurseContent> items = nurseService.getCustomerNurseItems(customer.getId());
        if (items == null || items.isEmpty()) {
            System.out.println("该客户暂无已购买的护理项目。");
            waitEnter();
            return;
        }

        // 显示可选的护理项目
        System.out.println("可执行护理的项目:");
        System.out.printf("%-5s %-15s %-10s %-8s\n", "序号", "护理名称", "执行周期", "执行次数");
        printDivider();
        for (int i = 0; i < items.size(); i++) {
            NurseContent nc = items.get(i);
            System.out.printf("%-5d %-15s %-10s %-8s\n",
                    (i + 1), nc.getNursingName(), nc.getExecutionCycle(), nc.getExecutionTimes());
        }
        System.out.println("  0. 返回");

        int choice = InputUtil.readInt("请选择护理项目: ");
        if (choice == 0) return;
        if (choice < 1 || choice > items.size()) {
            System.out.println("无效选项！");
            waitEnter();
            return;
        }

        NurseContent selectedItem = items.get(choice - 1);

        // 填写护理记录
        printHeader("填写护理记录");
        System.out.println("护理项目: " + selectedItem.getNursingName());

        NurseRecord record = new NurseRecord();
        record.setCustomerId(customer.getId());
        record.setItemId(selectedItem.getId());
        record.setUserId(currentUser.getId());
        record.setIsDeleted(0);

        String nursingTime = InputUtil.readString("护理时间(yyyy-MM-dd HH:mm, 直接回车为当前时间): ", "");
        if (nursingTime.isEmpty()) {
            nursingTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        }
        record.setNursingTime(nursingTime);

        record.setNursingContent(InputUtil.readString("护理内容描述: "));
        record.setNursingCount(InputUtil.readInt("护理次数: ", 1, 999));

        nurseRecordService.addRecord(record);
        System.out.println("\n护理记录提交成功！");
        waitEnter();
    }
}
