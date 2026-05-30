package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.*;
import com.neuedu.tms.service.*;
import com.neuedu.tms.utils.InputUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 客户管理视图 - 管理员管理客户的所有操作
 * 包括客户列表、查找、入住登记、修改、删除、分配管家、服务关注等功能
 */
public class CustomerManageView extends BaseView implements MenuItem {

    private CustomerService customerService = new CustomerService();
    private BedService bedService = new BedService();
    private UserService userService = new UserService();
    private NurseService nurseService = new NurseService();
    private CustomerNurseItemService customerNurseItemService = new CustomerNurseItemService();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void execute() {
        while (true) {
            printHeader("客户管理");
            System.out.println("  1. 客户列表（全部）");
            System.out.println("  2. 客户列表（自理老人）");
            System.out.println("  3. 客户列表（护理老人）");
            System.out.println("  4. 查找客户");
            System.out.println("  5. 入住登记");
            System.out.println("  6. 修改客户信息");
            System.out.println("  7. 删除客户");
            System.out.println("  8. 设置服务对象（分配健康管家）");
            System.out.println("  9. 服务关注");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: listAllCustomers(); break;
                    case 2: listSelfcareCustomers(); break;
                    case 3: listNursingCustomers(); break;
                    case 4: searchCustomer(); break;
                    case 5: addCustomer(); break;
                    case 6: updateCustomer(); break;
                    case 7: deleteCustomer(); break;
                    case 8: assignSteward(); break;
                    case 9: serviceAttention(); break;
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
     * 显示全部客户列表
     */
    private void listAllCustomers() {
        printHeader("客户列表（全部）");
        List<Customer> list = customerService.listAll();
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            printCustomerTable(list);
        }
        waitEnter();
    }

    /**
     * 显示自理老人客户列表（没有护理等级的客户）
     */
    private void listSelfcareCustomers() {
        printHeader("客户列表（自理老人）");
        List<Customer> list = customerService.listByType("selfcare");
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            printCustomerTable(list);
        }
        waitEnter();
    }

    /**
     * 显示护理老人客户列表（有护理等级的客户）
     */
    private void listNursingCustomers() {
        printHeader("客户列表（护理老人）");
        List<Customer> list = customerService.listByType("nursing");
        if (list == null || list.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            printCustomerTable(list);
        }
        waitEnter();
    }

    /**
     * 根据姓名模糊搜索客户
     */
    private void searchCustomer() {
        printHeader("查找客户");
        String keyword = InputUtil.readString("请输入客户姓名（支持模糊匹配）: ");
        List<Customer> list = customerService.searchByName(keyword);
        if (list == null || list.isEmpty()) {
            System.out.println("未找到匹配的客户。");
        } else {
            System.out.println("找到 " + list.size() + " 个匹配客户:");
            printCustomerTable(list);
        }
        waitEnter();
    }

    /**
     * 入住登记 - 添加新客户
     */
    private void addCustomer() {
        printHeader("入住登记");
        Customer customer = new Customer();

        customer.setCustomerName(InputUtil.readString("姓名: "));
        customer.setCustomerSex(InputUtil.readInt("性别(0-女, 1-男): ", 0, 1));
        customer.setIdcard(InputUtil.readString("身份证号: "));

        // 展示有空闲床位的房间，自动分配
        System.out.println("\n--- 空闲床位一览 ---");
        List<Room> rooms = bedService.listAllRooms();
        boolean hasAvailable = false;
        for (Room room : rooms) {
            List<Bed> available = bedService.listAvailableByRoomNo(room.getRoomNo());
            if (available != null && !available.isEmpty()) {
                hasAvailable = true;
                System.out.printf("  [%s] 房间号: %d  空闲床位: %d个\n",
                        room.getRoomFloor(), room.getRoomNo(), available.size());
                for (Bed bed : available) {
                    System.out.printf("    床位ID: %d  编号: %s\n", bed.getId(), bed.getBedNo());
                }
            }
        }
        if (!hasAvailable) {
            System.out.println("当前没有空闲床位！登记失败。");
            waitEnter();
            return;
        }

        // 选择床位
        int bedId = InputUtil.readInt("请选择床位ID (0返回): ");
        if (bedId == 0) return;
        Bed selectedBed = bedService.getById(bedId);
        if (selectedBed == null || selectedBed.getBedStatus() != BedStatus.FREE.getCode()) {
            System.out.println("该床位不可用！登记失败。");
            waitEnter();
            return;
        }
        customer.setBedId(bedId);
        customer.setRoomNo(String.valueOf(selectedBed.getRoomNo()));

        customer.setBirthday(InputUtil.readString("出生日期(yyyy-MM-dd): "));
        customer.setContactTel(InputUtil.readString("联系电话: "));
        customer.setFamilyMember(InputUtil.readString("家属姓名: "));

        LocalDate today = LocalDate.now();
        customer.setCheckinDate(today.format(DATE_FORMATTER));

        customer.setHeight(InputUtil.readString("身高(cm), 直接回车跳过: ", ""));
        customer.setWeight(InputUtil.readString("体重(kg), 直接回车跳过: ", ""));
        customer.setBloodType(InputUtil.readString("血型, 直接回车跳过: ", ""));
        customer.setPsychosomaticState(InputUtil.readString("身心状况备注, 直接回车跳过: ", ""));

        Customer result = customerService.addCustomer(customer);
        if (result != null) {
            // 床位设为有人
            bedService.updateBedStatus(bedId, 2);
            // 新增床位使用详情记录
            BedDetail bd = new BedDetail();
            bd.setCustomerId(result.getId());
            bd.setBedId(bedId);
            bd.setStartDate(today.format(DATE_FORMATTER));
            bd.setRemark("初次入住，房间号" + selectedBed.getRoomNo());
            bd.setIsDeleted(0);
            new BedDetailService().addBedDetail(bd);
            System.out.println("\n入住登记成功！客户ID: " + result.getId());
        } else {
            System.out.println("\n入住登记失败！");
        }
        waitEnter();
    }

    /**
     * 修改客户信息
     */
    private void updateCustomer() {
        printHeader("修改客户信息");
        int id = InputUtil.readInt("请输入要修改的客户ID (0返回): ");
        if (id == 0) return;
        Customer customer = customerService.getById(id);
        if (customer == null) {
            System.out.println("客户不存在！");
            waitEnter();
            return;
        }

        System.out.println("当前信息:");
        printCustomerDetail(customer);
        System.out.println("\n（直接回车保留原值，输入新值则更新）");

        String name = InputUtil.readString("姓名 [" + customer.getCustomerName() + "]: ", customer.getCustomerName());
        if (!name.equals(customer.getCustomerName())) {
            customer.setCustomerName(name);
        }

        String sexStr = InputUtil.readString("性别(0-女,1-男) [" + customer.getCustomerSex() + "]: ", String.valueOf(customer.getCustomerSex()));
        customer.setCustomerSex(Integer.parseInt(sexStr));

        String idcard = InputUtil.readString("身份证号 [" + customer.getIdcard() + "]: ", customer.getIdcard());
        if (!idcard.equals(customer.getIdcard())) {
            customer.setIdcard(idcard);
        }

        String tel = InputUtil.readString("联系电话 [" + customer.getContactTel() + "]: ", customer.getContactTel());
        if (!tel.equals(customer.getContactTel())) {
            customer.setContactTel(tel);
        }

        String birthday = InputUtil.readString("出生日期 [" + customer.getBirthday() + "]: ", customer.getBirthday());
        if (!birthday.equals(customer.getBirthday())) {
            customer.setBirthday(birthday);
        }

        String height = InputUtil.readString("身高 [" + (customer.getHeight() != null ? customer.getHeight() : "") + "]: ", customer.getHeight() != null ? customer.getHeight() : "");
        customer.setHeight(height);

        String weight = InputUtil.readString("体重 [" + (customer.getWeight() != null ? customer.getWeight() : "") + "]: ", customer.getWeight() != null ? customer.getWeight() : "");
        customer.setWeight(weight);

        String blood = InputUtil.readString("血型 [" + (customer.getBloodType() != null ? customer.getBloodType() : "") + "]: ", customer.getBloodType() != null ? customer.getBloodType() : "");
        customer.setBloodType(blood);

        String state = InputUtil.readString("身心状况 [" + (customer.getPsychosomaticState() != null ? customer.getPsychosomaticState() : "") + "]: ", customer.getPsychosomaticState() != null ? customer.getPsychosomaticState() : "");
        customer.setPsychosomaticState(state);

        Customer result = customerService.updateCustomer(customer);
        if (result != null) {
            System.out.println("\n客户信息修改成功！");
        } else {
            System.out.println("\n客户信息修改失败！");
        }
        waitEnter();
    }

    /**
     * 删除客户（逻辑删除，同时释放床位）
     */
    private void deleteCustomer() {
        printHeader("删除客户");
        int id = InputUtil.readInt("请输入要删除的客户ID (0返回): ");
        if (id == 0) return;
        Customer customer = customerService.getById(id);
        if (customer == null) {
            System.out.println("客户不存在！");
            waitEnter();
            return;
        }

        printCustomerDetail(customer);

        boolean confirm = InputUtil.readConfirm("确认删除该客户？");
        if (confirm) {
            customerService.deleteCustomer(id);
            System.out.println("客户已删除。");
        } else {
            System.out.println("已取消删除操作。");
        }
        waitEnter();
    }

    /**
     * 设置服务对象 - 为客户分配健康管家
     */
    private void assignSteward() {
        printHeader("设置服务对象（分配健康管家）");
        int customerId = InputUtil.readInt("请输入客户ID (0返回): ");
        if (customerId == 0) return;
        Customer customer = customerService.getById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            waitEnter();
            return;
        }

        System.out.println("客户: " + customer.getCustomerName());
        if (customer.getUserId() != null) {
            User currentSteward = userService.getById(customer.getUserId());
            System.out.println("当前管家: " + (currentSteward != null ? currentSteward.getNickname() : "未知"));
        } else {
            System.out.println("当前管家: 未分配");
        }

        System.out.println("\n--- 可选健康管家列表 ---");
        List<User> stewards = userService.listStewards();
        if (stewards == null || stewards.isEmpty()) {
            System.out.println("暂无可用健康管家。");
            waitEnter();
            return;
        }

        for (User s : stewards) {
            System.out.printf("  管家ID: %d  姓名: %s  电话: %s\n", s.getId(), s.getNickname(), s.getPhoneNumber());
        }

        int stewardId = InputUtil.readInt("请输入管家ID（输入0取消分配）: ");
        if (stewardId == 0) {
            userService.removeSteward(customerId);
            System.out.println("已取消健康管家分配。");
        } else {
            userService.assignSteward(customerId, stewardId);
            System.out.println("健康管家分配成功！");
        }
        waitEnter();
    }

    /**
     * 服务关注 - 查看客户的服务状态和购买记录
     */
    private void serviceAttention() {
        printHeader("服务关注");
        int customerId = InputUtil.readInt("请输入客户ID (0返回): ");
        if (customerId == 0) return;
        Customer customer = customerService.getById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            waitEnter();
            return;
        }

        System.out.println("客户: " + customer.getCustomerName());

        // 显示已购买的护理项目
        List<NurseContent> nurseItems = nurseService.getCustomerNurseItems(customerId);
        if (nurseItems == null || nurseItems.isEmpty()) {
            System.out.println("该客户暂无已购买的护理项目。");
        } else {
            System.out.println("\n--- 已购护理项目 ---");
            System.out.printf("%-5s %-10s %-15s %-10s %-15s\n", "ID", "编号", "护理名称", "价格", "执行周期");
            printDivider();
            for (NurseContent nc : nurseItems) {
                System.out.printf("%-5d %-10s %-15s %-10s %-15s\n",
                        nc.getId(), nc.getSerialNumber(), nc.getNursingName(),
                        nc.getServicePrice(), nc.getExecutionCycle());
            }
        }

        // 显示服务状态
        java.util.List<java.util.Map<String, Object>> statusList = customerNurseItemService.getServiceStatus(customerId);
        if (statusList != null && !statusList.isEmpty()) {
            System.out.println("\n--- 服务状态 ---");
            System.out.printf("%-5s %-15s %-10s %-10s %-12s %-12s\n", "ID", "项目名称", "护理次数", "状态", "购买日期", "到期日期");
            printDivider();
            for (java.util.Map<String, Object> m : statusList) {
                System.out.printf("%-5s %-15s %-10s %-10s %-12s %-12s\n",
                        m.get("id"), m.get("itemName"), m.get("nurseNumber"),
                        m.get("status"), m.get("buyTime"), m.get("maturityTime"));
            }
        }

        waitEnter();
    }

    // ==================== 辅助显示方法 ====================

    /**
     * 以表格形式打印客户列表
     */
    private void printCustomerTable(List<Customer> list) {
        System.out.printf("%-5s %-10s %-3s %-5s %-8s %-12s %-12s %-12s\n",
                "ID", "姓名", "年龄", "性别", "房间号", "入住日期", "联系电话", "身心状况");
        printDivider();
        for (Customer c : list) {
            String sexText = Gender.textOf(c.getCustomerSex());
            System.out.printf("%-5d %-10s %-3d %-5s %-8s %-12s %-12s %-12s\n",
                    c.getId(), c.getCustomerName(), c.getCustomerAge(), sexText,
                    c.getRoomNo() != null ? c.getRoomNo() : "-",
                    c.getCheckinDate() != null ? c.getCheckinDate() : "-",
                    c.getContactTel() != null ? c.getContactTel() : "-",
                    c.getPsychosomaticState() != null && !c.getPsychosomaticState().isEmpty()
                            ? c.getPsychosomaticState() : "无");
        }
    }

    /**
     * 打印单个客户的详细信息
     */
    private void printCustomerDetail(Customer c) {
        printDivider();
        System.out.println("  客户ID: " + c.getId());
        System.out.println("  姓名: " + c.getCustomerName());
        System.out.println("  年龄: " + c.getCustomerAge());
        System.out.println("  性别: " + Gender.textOf(c.getCustomerSex()));
        System.out.println("  身份证号: " + c.getIdcard());
        System.out.println("  楼号: " + (c.getBuildingNo() != null ? c.getBuildingNo() : "-"));
        System.out.println("  房间号: " + (c.getRoomNo() != null ? c.getRoomNo() : "-"));
        System.out.println("  床位ID: " + c.getBedId());
        System.out.println("  入住日期: " + (c.getCheckinDate() != null ? c.getCheckinDate() : "-"));
        System.out.println("  联系电话: " + (c.getContactTel() != null ? c.getContactTel() : "-"));
        System.out.println("  出生日期: " + (c.getBirthday() != null ? c.getBirthday() : "-"));
        System.out.println("  身高: " + (c.getHeight() != null ? c.getHeight() : "-"));
        System.out.println("  体重: " + (c.getWeight() != null ? c.getWeight() : "-"));
        System.out.println("  血型: " + (c.getBloodType() != null ? c.getBloodType() : "-"));
        System.out.println("  家属: " + (c.getFamilyMember() != null ? c.getFamilyMember() : "-"));
        System.out.println("  身心状况: " + (c.getPsychosomaticState() != null && !c.getPsychosomaticState().isEmpty() ? c.getPsychosomaticState() : "无"));
        System.out.println("  管家ID: " + (c.getUserId() != null ? c.getUserId() : "未分配"));
        System.out.println("  护理等级ID: " + (c.getLevelId() != null ? c.getLevelId() : "未设置"));
    }

    /**
     * 床位状态文本
     */
    private String bedStatusText(int status) {
        return BedStatus.textOf(status);
    }
}
