package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Backdown;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.service.BackdownService;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.utils.InputUtil;
import com.neuedu.tms.enums.AuditStatus;
import com.neuedu.tms.enums.RetreatType;

import java.util.List;

/**
 * 退住登记管理视图 - 管理员管理退住申请和审批
 */
public class BackdownManageView extends BaseView implements MenuItem {

    private BackdownService backdownService = new BackdownService();
    private CustomerService customerService = new CustomerService();

    @Override
    public void execute() {
        while (true) {
            printHeader("退住登记管理");
            System.out.println("  1. 退住申请列表");
            System.out.println("  2. 审批退住申请（通过/拒绝）");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listBackdown(); break;
                    case 2: auditBackdown(); break;
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
     * 列出所有退住申请
     */
    private void listBackdown() {
        printHeader("退住申请列表");
        List<Backdown> list = backdownService.listAll();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-10s %-12s %-10s %-8s %-10s %-15s\n",
                    "ID", "客户ID", "退住时间", "退住类型", "审批状态", "审批人", "原因");
            printDivider();
            for (Backdown b : list) {
                String typeText = RetreatType.textOf(b.getRetreatType());
                String statusText = AuditStatus.textOf(b.getAuditStatus());
                System.out.printf("%-5d %-10d %-12s %-10s %-8s %-10s %-15s\n",
                        b.getId(), b.getCustomerId(),
                        b.getRetreatTime() != null ? b.getRetreatTime() : "-",
                        typeText, statusText,
                        b.getAuditPerson() != null ? b.getAuditPerson() : "-",
                        b.getRetreatReason() != null ? b.getRetreatReason() : "-");
            }
        }
        waitEnter();
    }

    /**
     * 审批退住申请
     */
    private void auditBackdown() {
        printHeader("审批退住申请");
        int id = InputUtil.readInt("请输入退住申请ID (0返回): ");
        if (id == 0) return;
        Backdown backdown = backdownService.getById(id);
        if (backdown == null) {
            System.out.println("退住申请不存在！");
            waitEnter();
            return;
        }

        Customer customer = customerService.getById(backdown.getCustomerId());
        System.out.println("客户: " + (customer != null ? customer.getCustomerName() : "未知"));

        String typeText = RetreatType.textOf(backdown.getRetreatType());
        System.out.println("退住时间: " + backdown.getRetreatTime());
        System.out.println("退住类型: " + typeText);
        System.out.println("退住原因: " + backdown.getRetreatReason());
        System.out.println("当前状态: " + AuditStatus.textOf(backdown.getAuditStatus()));

        if (backdown.getAuditStatus() != AuditStatus.SUBMITTED.getCode()) {
            System.out.println("该申请已审批过，无法再次审批。");
            waitEnter();
            return;
        }

        System.out.println("\n  1. 通过");
        System.out.println("  2. 拒绝");
        int action = InputUtil.readInt("请选择: ", 1, 2);
        String auditor = InputUtil.readString("审批人姓名: ");

        backdownService.auditBackdown(id, action, auditor);
        System.out.println("\n审批操作完成！");
        waitEnter();
    }
}
