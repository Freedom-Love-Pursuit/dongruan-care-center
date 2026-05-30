package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.Outward;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.service.OutwardService;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 外出登记管理视图 - 管理员管理外出申请、审批和回院登记
 */
public class OutwardManageView extends BaseView implements MenuItem {

    private OutwardService outwardService = new OutwardService();
    private CustomerService customerService = new CustomerService();

    @Override
    public void execute() {
        while (true) {
            printHeader("外出登记管理");
            System.out.println("  1. 外出申请列表");
            System.out.println("  2. 审批外出申请（通过/拒绝）");
            System.out.println("  3. 登记回院时间");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listOutward(); break;
                    case 2: auditOutward(); break;
                    case 3: registerReturn(); break;
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
     * 列出所有外出申请
     */
    private void listOutward() {
        printHeader("外出申请列表");
        List<Outward> list = outwardService.listAll();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-10s %-12s %-12s %-12s %-8s %-10s\n",
                    "ID", "客户ID", "外出时间", "预计返回", "实际返回", "审批状态", "陪同人");
            printDivider();
            for (Outward o : list) {
                String statusText = AuditStatus.textOf(o.getAuditStatus());
                System.out.printf("%-5d %-10d %-12s %-12s %-12s %-8s %-10s\n",
                        o.getId(), o.getCustomerId(),
                        o.getOutgoingTime() != null ? o.getOutgoingTime() : "-",
                        o.getExpectedReturnTime() != null ? o.getExpectedReturnTime() : "-",
                        o.getActualReturnTime() != null ? o.getActualReturnTime() : "未返回",
                        statusText,
                        o.getEscorted() != null ? o.getEscorted() : "-");
            }
        }
        waitEnter();
    }

    /**
     * 审批外出申请
     */
    private void auditOutward() {
        printHeader("审批外出申请");
        int id = InputUtil.readInt("请输入外出申请ID (0返回): ");
        if (id == 0) return;
        Outward outward = outwardService.getById(id);
        if (outward == null) {
            System.out.println("外出申请不存在！");
            waitEnter();
            return;
        }

        // 显示申请详情
        Customer customer = customerService.getById(outward.getCustomerId());
        System.out.println("客户: " + (customer != null ? customer.getCustomerName() : "未知"));
        System.out.println("外出原因: " + outward.getOutgoingReason());
        System.out.println("外出时间: " + outward.getOutgoingTime());
        System.out.println("预计返回: " + outward.getExpectedReturnTime());
        System.out.println("陪同人: " + outward.getEscorted() + " (关系: " + outward.getRelation() + ")");
        System.out.println("当前状态: " + auditStatusText(outward.getAuditStatus()));

        if (outward.getAuditStatus() != 0) {
            System.out.println("该申请已审批过，无法再次审批。");
            waitEnter();
            return;
        }

        System.out.println("\n  1. 通过");
        System.out.println("  2. 拒绝");
        int action = InputUtil.readInt("请选择: ", 1, 2);
        String auditor = InputUtil.readString("审批人姓名: ");

        outwardService.auditOutward(id, action, auditor);
        System.out.println("\n审批操作完成！");
        waitEnter();
    }

    /**
     * 登记回院时间
     */
    private void registerReturn() {
        printHeader("登记回院时间");
        int id = InputUtil.readInt("请输入外出申请ID (0返回): ");
        if (id == 0) return;
        Outward outward = outwardService.getById(id);
        if (outward == null) {
            System.out.println("外出申请不存在！");
            waitEnter();
            return;
        }

        if (outward.getAuditStatus() != 1) {
            System.out.println("该申请尚未审批通过，无法登记回院。");
            waitEnter();
            return;
        }

        Customer customer = customerService.getById(outward.getCustomerId());
        System.out.println("客户: " + (customer != null ? customer.getCustomerName() : "未知"));
        System.out.println("外出时间: " + outward.getOutgoingTime());
        System.out.println("预计返回: " + outward.getExpectedReturnTime());

        String returnTime = InputUtil.readString("请输入实际回院时间(yyyy-MM-dd HH:mm): ");
        outwardService.registerReturn(id, returnTime);
        System.out.println("\n回院登记成功！");
        waitEnter();
    }

    /**
     * 审核状态文本
     */
    private String auditStatusText(int status) {
        return AuditStatus.textOf(status);
    }
}
