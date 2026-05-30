package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Backdown;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.BackdownService;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.utils.InputUtil;
import com.neuedu.tms.enums.RetreatType;
import com.neuedu.tms.enums.AuditStatus;

import java.util.List;

/**
 * 健康管家 - 退住申请视图
 * 选择客户 → 填写退住表单 → 提交申请
 */
public class BackdownApplyView extends BaseView implements MenuItem {

    private User currentUser;
    private CustomerService customerService = new CustomerService();
    private BackdownService backdownService = new BackdownService();

    /**
     * 构造退住申请视图。
     * @param currentUser 当前登录的健康管家用户
     */
    public BackdownApplyView(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 进入退住申请流程。
     * 实现：先选择一个客户，然后填写退住类型、时间、原因等表单信息，提交申请等待审批。
     */
    @Override
    public void execute() {
        printHeader("退住申请");

        // 选择客户
        Customer customer = selectCustomer();
        if (customer == null) {
            return;
        }

        // 填写退住申请表单
        Backdown backdown = new Backdown();
        backdown.setCustomerId(customer.getId());
        backdown.setIsDeleted(0);
        backdown.setAuditStatus(AuditStatus.SUBMITTED.getCode()); // 已提交待审批

        System.out.println("\n客户: " + customer.getCustomerName());
        System.out.println("请填写退住信息:");

        backdown.setRetreatTime(InputUtil.readString("退住时间(yyyy-MM-dd HH:mm): "));

        System.out.println("退住类型:");
        for (RetreatType t : RetreatType.values()) {
            System.out.printf("  %d - %s\n", t.getCode(), t.getText());
        }
        int retreatType = InputUtil.readInt("请选择(0/1/2): ", 0, 2);
        backdown.setRetreatType(retreatType);

        backdown.setRetreatReason(InputUtil.readString("退住原因: "));
        backdown.setRemarks(InputUtil.readString("备注说明(直接回车跳过): ", ""));

        backdownService.addBackdown(backdown);
        System.out.println("\n退住申请已提交，等待管理员审批！");
        waitEnter();
    }

    /**
     * 选择客户
     */
    private Customer selectCustomer() {
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
}
