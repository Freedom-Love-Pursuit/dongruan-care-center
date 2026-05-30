package com.neuedu.tms.view;

import com.neuedu.tms.pojo.*;
import com.neuedu.tms.service.*;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 护理管理视图 - 管理员管理护理项目、护理等级和客户护理设置
 */
public class NurseManageView extends BaseView implements MenuItem {

    private NurseService nurseService = new NurseService();
    private NurseContentService nurseContentService = new NurseContentService();
    private NurseLevelService nurseLevelService = new NurseLevelService();
    private CustomerNurseItemService customerNurseItemService = new CustomerNurseItemService();
    private CustomerService customerService = new CustomerService();

    @Override
    public void execute() {
        while (true) {
            printHeader("护理管理");
            System.out.println("  1. 护理项目管理");
            System.out.println("  2. 护理等级管理");
            System.out.println("  3. 客户护理设置");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: manageNurseItems(); break;
                    case 2: manageNurseLevels(); break;
                    case 3: customerNurseSettings(); break;
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

    // ==================== 护理项目管理 ====================

    /**
     * 护理项目管理子菜单
     */
    private void manageNurseItems() {
        while (true) {
            printHeader("护理项目管理");
            System.out.println("  1. 列表");
            System.out.println("  2. 添加");
            System.out.println("  3. 修改");
            System.out.println("  4. 删除");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listNurseItems(); break;
                    case 2: addNurseItem(); break;
                    case 3: updateNurseItem(); break;
                    case 4: deleteNurseItem(); break;
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
     * 列出所有护理项目
     */
    private void listNurseItems() {
        printHeader("护理项目列表");
        List<NurseContent> list = nurseService.listNurseItems();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-10s %-15s %-10s %-10s %-8s %-12s\n",
                    "ID", "编号", "护理名称", "价格", "状态", "执行周期", "执行次数");
            printDivider();
            for (NurseContent nc : list) {
                System.out.printf("%-5d %-10s %-15s %-10s %-10s %-8s %-12s\n",
                        nc.getId(), nc.getSerialNumber(), nc.getNursingName(),
                        nc.getServicePrice(),
                        nc.getStatus() == 1 ? "启用" : "停用",
                        nc.getExecutionCycle(),
                        nc.getExecutionTimes());
            }
        }
        waitEnter();
    }

    /**
     * 添加护理项目
     */
    private void addNurseItem() {
        printHeader("添加护理项目");
        NurseContent nc = new NurseContent();

        nc.setNursingName(InputUtil.readString("护理名称: "));
        nc.setServicePrice(InputUtil.readString("服务价格: "));
        nc.setMessage(InputUtil.readString("备注说明: "));
        nc.setExecutionCycle(InputUtil.readString("执行周期: "));
        nc.setExecutionTimes(InputUtil.readString("执行次数: "));
        nc.setStatus(1);  // 默认启用
        nc.setIsDeleted(0);

        boolean result = nurseService.addNurseItem(nc);
        if (result) {
            System.out.println("\n护理项目添加成功！");
        } else {
            System.out.println("\n护理项目添加失败！");
        }
        waitEnter();
    }

    /**
     * 修改护理项目
     */
    private void updateNurseItem() {
        printHeader("修改护理项目");
        int id = InputUtil.readInt("请输入要修改的护理项目ID (0返回): ");
        if (id == 0) return;
        NurseContent nc = nurseService.findNurseItemById(id);
        if (nc == null) {
            System.out.println("护理项目不存在！");
            waitEnter();
            return;
        }

        System.out.println("当前信息: " + nc.getSerialNumber() + " - " + nc.getNursingName());
        System.out.println("（直接回车保留原值）");

        String name = InputUtil.readString("护理名称 [" + nc.getNursingName() + "]: ", nc.getNursingName());
        nc.setNursingName(name);

        String price = InputUtil.readString("服务价格 [" + nc.getServicePrice() + "]: ", nc.getServicePrice());
        nc.setServicePrice(price);

        String msg = InputUtil.readString("备注说明 [" + (nc.getMessage() != null ? nc.getMessage() : "") + "]: ", nc.getMessage() != null ? nc.getMessage() : "");
        nc.setMessage(msg);

        String cycle = InputUtil.readString("执行周期 [" + nc.getExecutionCycle() + "]: ", nc.getExecutionCycle());
        nc.setExecutionCycle(cycle);

        String times = InputUtil.readString("执行次数 [" + nc.getExecutionTimes() + "]: ", nc.getExecutionTimes());
        nc.setExecutionTimes(times);

        int status = InputUtil.readInt("状态(1-启用, 2-停用) [" + nc.getStatus() + "]: ", 1, 2);
        nc.setStatus(status);

        boolean result = nurseService.updateNurseItem(nc);
        if (result) {
            System.out.println("\n护理项目修改成功！");
        } else {
            System.out.println("\n护理项目修改失败！");
        }
        waitEnter();
    }

    /**
     * 删除护理项目
     */
    private void deleteNurseItem() {
        printHeader("删除护理项目");
        int id = InputUtil.readInt("请输入要删除的护理项目ID (0返回): ");
        if (id == 0) return;
        NurseContent nc = nurseService.findNurseItemById(id);
        if (nc == null) {
            System.out.println("护理项目不存在！");
            waitEnter();
            return;
        }

        System.out.println("护理项目: " + nc.getSerialNumber() + " - " + nc.getNursingName());

        boolean confirm = InputUtil.readConfirm("确认删除？");
        if (confirm) {
            boolean result = nurseService.deleteNurseItem(id);
            if (result) {
                System.out.println("护理项目已删除。");
            } else {
                System.out.println("护理项目删除失败！");
            }
        } else {
            System.out.println("已取消删除操作。");
        }
        waitEnter();
    }

    // ==================== 护理等级管理 ====================

    /**
     * 护理等级管理子菜单
     */
    private void manageNurseLevels() {
        while (true) {
            printHeader("护理等级管理");
            System.out.println("  1. 列表");
            System.out.println("  2. 添加");
            System.out.println("  3. 修改");
            System.out.println("  4. 删除");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listNurseLevels(); break;
                    case 2: addNurseLevel(); break;
                    case 3: updateNurseLevel(); break;
                    case 4: deleteNurseLevel(); break;
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
     * 列出所有护理等级
     */
    private void listNurseLevels() {
        printHeader("护理等级列表");
        List<NurseLevel> list = nurseService.listNurseLevels();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-15s %-10s\n", "ID", "等级名称", "状态");
            printDivider();
            for (NurseLevel nl : list) {
                System.out.printf("%-5d %-15s %-10s\n",
                        nl.getId(), nl.getLevelName(),
                        nl.getLevelStatus() == 1 ? "启用" : "停用");
            }
        }
        waitEnter();
    }

    /**
     * 添加护理等级
     */
    private void addNurseLevel() {
        printHeader("添加护理等级");
        NurseLevel nl = new NurseLevel();

        nl.setLevelName(InputUtil.readString("等级名称: "));
        nl.setLevelStatus(1);  // 默认启用
        nl.setIsDeleted(0);

        boolean result = nurseService.addNurseLevel(nl);
        if (result) {
            System.out.println("\n护理等级添加成功！");
        } else {
            System.out.println("\n护理等级添加失败！");
        }
        waitEnter();
    }

    /**
     * 修改护理等级
     */
    private void updateNurseLevel() {
        printHeader("修改护理等级");
        int id = InputUtil.readInt("请输入要修改的护理等级ID (0返回): ");
        if (id == 0) return;
        NurseLevel nl = nurseService.findNurseLevelById(id);
        if (nl == null) {
            System.out.println("护理等级不存在！");
            waitEnter();
            return;
        }

        System.out.println("当前信息: " + nl.getLevelName());
        System.out.println("（直接回车保留原值）");

        String name = InputUtil.readString("等级名称 [" + nl.getLevelName() + "]: ", nl.getLevelName());
        nl.setLevelName(name);

        int status = InputUtil.readInt("状态(1-启用, 2-停用) [" + nl.getLevelStatus() + "]: ", 1, 2);
        nl.setLevelStatus(status);

        boolean result = nurseService.updateNurseLevel(nl);
        if (result) {
            System.out.println("\n护理等级修改成功！");
        } else {
            System.out.println("\n护理等级修改失败！");
        }
        waitEnter();
    }

    /**
     * 删除护理等级
     */
    private void deleteNurseLevel() {
        printHeader("删除护理等级");
        int id = InputUtil.readInt("请输入要删除的护理等级ID (0返回): ");
        if (id == 0) return;
        NurseLevel nl = nurseService.findNurseLevelById(id);
        if (nl == null) {
            System.out.println("护理等级不存在！");
            waitEnter();
            return;
        }

        System.out.println("护理等级: " + nl.getLevelName());

        boolean confirm = InputUtil.readConfirm("确认删除？");
        if (confirm) {
            boolean result = nurseService.deleteNurseLevel(id);
            if (result) {
                System.out.println("护理等级已删除。");
            } else {
                System.out.println("护理等级删除失败！");
            }
        } else {
            System.out.println("已取消删除操作。");
        }
        waitEnter();
    }

    // ==================== 客户护理设置 ====================

    /**
     * 为客户设置护理等级
     */
    private void customerNurseSettings() {
        printHeader("客户护理设置");
        int customerId = InputUtil.readInt("请输入客户ID (0返回): ");
        if (customerId == 0) return;
        com.neuedu.tms.pojo.Customer customer = customerService.getById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            waitEnter();
            return;
        }

        System.out.println("客户: " + customer.getCustomerName());
        if (customer.getLevelId() != null) {
            NurseLevel currentLevel = nurseLevelService.getById(customer.getLevelId());
            System.out.println("当前护理等级: " + (currentLevel != null ? currentLevel.getLevelName() : "未知"));
        } else {
            System.out.println("当前护理等级: 未设置");
        }

        System.out.println("\n--- 可选护理等级 ---");
        List<NurseLevel> levels = nurseService.listNurseLevels();
        if (levels == null || levels.isEmpty()) {
            System.out.println("暂无可用护理等级。");
            waitEnter();
            return;
        }

        for (NurseLevel nl : levels) {
            System.out.printf("  等级ID: %d  等级名称: %s  状态: %s\n",
                    nl.getId(), nl.getLevelName(),
                    nl.getLevelStatus() == 1 ? "启用" : "停用");
        }

        int levelId = InputUtil.readInt("请选择护理等级ID（输入0取消）: ");
        if (levelId == 0) {
            customerNurseItemService.removeCustomerLevel(customerId);
            System.out.println("已取消护理等级设置。");
        } else {
            customerNurseItemService.setCustomerLevel(customerId, levelId);
            System.out.println("护理等级设置成功！系统中的护理项目已自动分配到该客户。");
        }
        waitEnter();
    }
}
