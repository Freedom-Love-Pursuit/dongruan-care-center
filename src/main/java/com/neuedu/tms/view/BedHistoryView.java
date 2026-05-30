package com.neuedu.tms.view;

import com.neuedu.tms.pojo.*;
import com.neuedu.tms.service.*;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 床位历史视图 - 查看客户的床位使用历史记录
 * 在客户管理 -> 服务关注中可进入此视图
 */
public class BedHistoryView extends BaseView implements MenuItem {

    private BedDetailService bedDetailService = new BedDetailService();
    private BedService bedService = new BedService();

    private int customerId;
    private String customerName;

    /**
     * 构造床位历史视图。
     * @param customerId 客户ID
     * @param customerName 客户姓名
     */
    public BedHistoryView(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    /**
     * 显示客户床位使用历史记录。
     * 实现：查bedDetailService获取该客户的床位记录列表，表格输出或提示暂无记录。
     */
    @Override
    public void execute() {
        printHeader("床位使用历史 - " + customerName);
        List<BedDetail> list = bedDetailService.listByCustomerId(customerId);

        if (list == null || list.isEmpty()) {
            System.out.println("该客户暂无床位使用记录。");
            // 自动创建一条当前使用中的记录
            System.out.println("（系统会在客户入住登记时自动生成床位记录）");
        } else {
            System.out.printf("%-5s %-8s %-14s %-14s %-10s %-10s\n",
                    "ID", "床位号", "开始日期", "结束日期", "使用状态", "备注");
            printDivider();
            for (BedDetail bd : list) {
                Bed bed = bedService.getById(bd.getBedId());
                String bedNo = bed != null ? bed.getBedNo() : String.valueOf(bd.getBedId());
                String endDate = bd.getEndDate() != null ? bd.getEndDate() : "--";
                String status = bd.getEndDate() == null ? "使用中" : "已结束";

                System.out.printf("%-5d %-8s %-14s %-14s %-10s %-10s\n",
                        bd.getId(), bedNo,
                        bd.getStartDate() != null ? bd.getStartDate() : "-",
                        endDate, status,
                        bd.getRemark() != null ? bd.getRemark() : "");
            }
        }
        waitEnter();
    }
}
