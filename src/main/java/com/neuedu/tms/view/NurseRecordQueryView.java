package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.NurseRecord;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.service.NurseRecordService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 健康管家 - 护理记录查询视图
 * 选择客户 → 显示护理记录 → 可删除记录
 */
public class NurseRecordQueryView extends BaseView implements MenuItem {

    private User currentUser;
    private CustomerService customerService = new CustomerService();
    private NurseRecordService nurseRecordService = new NurseRecordService();

    /**
     * 构造护理记录查询视图。
     * @param currentUser 当前登录的健康管家用户
     */
    public NurseRecordQueryView(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 进入护理记录查询循环。
     * 实现：死循环中先选客户，然后显示该客户的护理记录列表，可按序号删除记录。
     */
    @Override
    public void execute() {
        while (true) {
            // 选择客户
            Customer customer = selectCustomer();
            if (customer == null) {
                return; // 用户选择返回
            }

            // 查询护理记录
            queryRecords(customer);
        }
    }

    /**
     * 选择客户
     */
    private Customer selectCustomer() {
        printHeader("护理记录查询 - 选择客户");
        List<Customer> list = customerService.listBySteward(currentUser.getId());
        if (list == null || list.isEmpty()) {
            System.out.println("您当前没有分配的客户。");
            waitEnter();
            return null;
        }

        for (int i = 0; i < list.size(); i++) {
            Customer c = list.get(i);
            System.out.printf("  %d. %s (房间: %s)\n",
                    (i + 1), c.getCustomerName(),
                    c.getRoomNo() != null ? c.getRoomNo() : "-");
        }
        System.out.println("  0. 返回");

        int choice = InputUtil.readInt("请选择客户: ");
        if (choice == 0) return null;
        if (choice >= 1 && choice <= list.size()) {
            return list.get(choice - 1);
        }
        System.out.println("无效选项！");
        waitEnter();
        return null;
    }

    /**
     * 查询并显示护理记录
     */
    private void queryRecords(Customer customer) {
        printHeader("护理记录 - " + customer.getCustomerName());

        List<NurseRecord> records = nurseRecordService.listByCustomerId(customer.getId());
        if (records == null || records.isEmpty()) {
            System.out.println("该客户暂无护理记录。");
            waitEnter();
            return;
        }

        System.out.printf("%-5s %-8s %-16s %-8s %-20s\n",
                "序号", "记录ID", "护理时间", "护理次数", "护理内容");
        printDivider();
        for (int i = 0; i < records.size(); i++) {
            NurseRecord r = records.get(i);
            System.out.printf("%-5d %-8d %-16s %-8d %-20s\n",
                    (i + 1), r.getId(),
                    r.getNursingTime() != null ? r.getNursingTime() : "-",
                    r.getNursingCount(),
                    r.getNursingContent() != null ? r.getNursingContent() : "-");
        }

        System.out.println("\n输入记录序号删除，输入 0 返回");
        int choice = InputUtil.readInt("请选择: ");

        if (choice == 0) return;
        if (choice >= 1 && choice <= records.size()) {
            NurseRecord selected = records.get(choice - 1);
            boolean confirm = InputUtil.readConfirm("确认删除该护理记录？");
            if (confirm) {
                nurseRecordService.deleteRecord(selected.getId());
                System.out.println("护理记录已删除。");
            } else {
                System.out.println("已取消删除操作。");
            }
        } else {
            System.out.println("无效选项！");
        }
        waitEnter();
    }
}
