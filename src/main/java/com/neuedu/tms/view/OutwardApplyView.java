package com.neuedu.tms.view;

import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.Outward;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.service.CustomerService;
import com.neuedu.tms.service.OutwardService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 健康管家 - 外出申请视图
 * 选择客户 → 填写外出表单 → 提交申请
 */
public class OutwardApplyView extends BaseView implements MenuItem {

    private User currentUser;
    private CustomerService customerService = new CustomerService();
    private OutwardService outwardService = new OutwardService();

    /**
     * 构造外出申请视图。
     * @param currentUser 当前登录的健康管家用户
     */
    public OutwardApplyView(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * 进入外出申请流程。
     * 实现：先选择一个客户，然后填写外出原因、时间、陪同人等表单信息，提交申请等待审批。
     */
    @Override
    public void execute() {
        printHeader("外出申请");

        // 选择客户
        Customer customer = selectCustomer();
        if (customer == null) {
            return;
        }

        // 填写外出申请表单
        Outward outward = new Outward();
        outward.setCustomerId(customer.getId());
        outward.setIsDeleted(0);
        outward.setAuditStatus(0); // 已提交待审批

        System.out.println("\n客户: " + customer.getCustomerName());
        System.out.println("请填写外出信息:");

        outward.setOutgoingReason(InputUtil.readString("外出原因: "));
        outward.setOutgoingTime(InputUtil.readString("外出时间(yyyy-MM-dd HH:mm): "));
        outward.setExpectedReturnTime(InputUtil.readString("预计返回时间(yyyy-MM-dd HH:mm): "));
        outward.setEscorted(InputUtil.readString("陪同人姓名: "));
        outward.setRelation(InputUtil.readString("陪同人与老人关系: "));
        outward.setEscortedTel(InputUtil.readString("陪同人电话: "));
        outward.setRemarks(InputUtil.readString("备注说明(直接回车跳过): ", ""));

        outwardService.addOutward(outward);
        System.out.println("\n外出申请已提交，等待管理员审批！");
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
