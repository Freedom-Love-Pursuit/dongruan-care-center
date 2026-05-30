package com.neuedu.tms.view;

import com.neuedu.tms.enums.*;
import com.neuedu.tms.pojo.Bed;
import com.neuedu.tms.pojo.Room;
import com.neuedu.tms.service.BedService;
import com.neuedu.tms.utils.InputUtil;

import java.util.List;

/**
 * 床位管理视图 - 管理员管理床位，包括统计、查看、调换等操作
 */
public class BedManageView extends BaseView implements MenuItem {

    private BedService bedService = new BedService();

    @Override
    public void execute() {
        while (true) {
            printHeader("床位管理");
            System.out.println("  1. 床位统计概览");
            System.out.println("  2. 按楼层查看床位");
            System.out.println("  3. 床位列表（全部）");
            System.out.println("  4. 床位调换");
            System.out.println("  0. 返回");
            printDivider();

            int choice = InputUtil.readInt("请选择操作: ");

            try {
                switch (choice) {
                    case 1: bedStats(); break;
                    case 2: viewBedsByFloor(); break;
                    case 3: listAllBeds(); break;
                    case 4: swapBed(); break;
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
     * 床位统计概览
     */
    private void bedStats() {
        printHeader("床位统计概览");
        int[] stats = bedService.getStats();
        System.out.println("  总床位数: " + stats[0]);
        System.out.println("  已占用:   " + stats[1]);
        System.out.println("  空闲:     " + stats[2]);
        System.out.println("  外出:     " + stats[3]);
        printDivider();
        if (stats[0] > 0) {
            double occupancyRate = (double) stats[1] / stats[0] * 100;
            System.out.printf("  入住率: %.1f%%\n", occupancyRate);
        }
        waitEnter();
    }

    /**
     * 按楼层查看床位
     */
    private void viewBedsByFloor() {
        printHeader("按楼层查看床位");
        List<Room> rooms = bedService.listAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("暂无房间数据");
            waitEnter();
            return;
        }

        // 按楼层分组显示
        String currentFloor = "";
        for (Room room : rooms) {
            String floor = room.getRoomFloor();
            if (!floor.equals(currentFloor)) {
                currentFloor = floor;
                System.out.println("\n--- " + floor + " ---");
            }
            List<Bed> beds = bedService.listByRoomNo(room.getRoomNo());
            if (beds != null) {
                for (Bed bed : beds) {
                    System.out.printf("  房间: %d  床位: %s  状态: %s\n",
                            bed.getRoomNo(), bed.getBedNo(), bedStatusText(bed.getBedStatus()));
                }
            }
        }
        waitEnter();
    }

    /**
     * 显示全部床位列表
     */
    private void listAllBeds() {
        printHeader("床位列表（全部）");
        List<Bed> beds = bedService.listAll();
        if (beds == null || beds.isEmpty()) {
            System.out.println("暂无数据");
        } else {
            System.out.printf("%-5s %-8s %-10s %-8s %-15s\n", "ID", "房间号", "床位编号", "状态", "备注");
            printDivider();
            for (Bed bed : beds) {
                System.out.printf("%-5d %-8d %-10s %-8s %-15s\n",
                        bed.getId(), bed.getRoomNo(), bed.getBedNo(),
                        bedStatusText(bed.getBedStatus()),
                        bed.getRemarks() != null ? bed.getRemarks() : "");
            }
        }
        waitEnter();
    }

    /**
     * 床位调换 - 将旧床位设为空闲，新床位设为有人
     */
    private void swapBed() {
        printHeader("床位调换");
        int oldBedId = InputUtil.readInt("请输入原床位ID (0返回): ");
        if (oldBedId == 0) return;
        Bed oldBed = bedService.getById(oldBedId);
        if (oldBed == null) {
            System.out.println("原床位不存在！");
            waitEnter();
            return;
        }
        System.out.println("原床位: 房间" + oldBed.getRoomNo() + " " + oldBed.getBedNo() + " [" + bedStatusText(oldBed.getBedStatus()) + "]");

        int newBedId = InputUtil.readInt("请输入目标床位ID (0返回): ");
        if (newBedId == 0) return;
        Bed newBed = bedService.getById(newBedId);
        if (newBed == null) {
            System.out.println("目标床位不存在！");
            waitEnter();
            return;
        }
        System.out.println("目标床位: 房间" + newBed.getRoomNo() + " " + newBed.getBedNo() + " [" + bedStatusText(newBed.getBedStatus()) + "]");

        if (oldBed.getBedStatus() != BedStatus.OCCUPIED.getCode()) {
            System.out.println("原床位不是'有人'状态，无法调换！");
            waitEnter();
            return;
        }

        if (oldBedId == newBedId) {
            System.out.println("不能换到同一床位！");
            waitEnter();
            return;
        }

        if (newBed.getBedStatus() != BedStatus.FREE.getCode()) {
            System.out.println("目标床位不是空闲状态，无法调换！");
            waitEnter();
            return;
        }

        boolean confirm = InputUtil.readConfirm("确认将床位从 " + oldBed.getBedNo() + " 调换到 " + newBed.getBedNo() + "？");
        if (confirm) {
            bedService.swapBed(oldBedId, newBedId);
            System.out.println("床位调换成功！");
        } else {
            System.out.println("已取消调换操作。");
        }
        waitEnter();
    }

    /**
     * 床位状态文本
     */
    private String bedStatusText(int status) {
        return BedStatus.textOf(status);
    }
}
