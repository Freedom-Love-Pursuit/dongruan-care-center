package com.neuedu.tms.service;

import com.neuedu.tms.dao.BedDao;
import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.enums.BedStatus;
import com.neuedu.tms.pojo.Bed;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.BedDetail;
import com.neuedu.tms.pojo.Room;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import com.neuedu.tms.utils.SortUtil;

public class BedService {

    private BedDao bedDao = new BedDao();

    /**
     * 查询所有床位。
     * 实现：调用bedDao.listAll查询全部，多条时按房间号和床位号排序。
     */
    public List<Bed> listAll() {
        try {
            List<Bed> beds = bedDao.listAll();
            if (beds != null && beds.size() > 1) {
                Collections.sort(beds, SortUtil.BY_ROOM_AND_BEDNO);
            }
            return beds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询床位。
     * 实现：调用bedDao.getById按ID查询并返回。
     */
    public Bed getById(int id) {
        try {
            return bedDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增床位。
     * 实现：调用bedDao.add写入床位记录。
     */
    public void addBed(Bed bed) {
        try {
            bedDao.add(bed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新床位信息。
     * 实现：调用bedDao.update更新床位记录。
     */
    public void updateBed(Bed bed) {
        try {
            bedDao.update(bed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新床位状态。
     * 实现：先调bedDao.getById获取床位，设新状态后调bedDao.update更新。
     */
    public void updateBedStatus(int id, int status) {
        try {
            Bed bed = bedDao.getById(id);
            if (bed != null) {
                bed.setBedStatus(status);
                bedDao.update(bed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据房间号查询所有床位。
     * 实现：调用bedDao.listByRoomNo查询，多条时按房间号和床位号排序。
     */
    public List<Bed> listByRoomNo(int roomNo) {
        try {
            List<Bed> beds = bedDao.listByRoomNo(roomNo);
            if (beds != null && beds.size() > 1) {
                Collections.sort(beds, SortUtil.BY_ROOM_AND_BEDNO);
            }
            return beds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据房间号查询可用床位（空闲状态）。
     * 实现：调用bedDao.listAvailableByRoomNo查询状态为1(空闲)的床位。
     */
    public List<Bed> listAvailableByRoomNo(int roomNo) {
        try {
            return bedDao.listAvailableByRoomNo(roomNo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取床位统计数据，返回[总数,已占用,空闲,外出]。
     * 实现：调用bedDao.listAll获取所有床位，遍历统计各状态数量。
     */
    public int[] getStats() {
        try {
            List<Bed> allBeds = bedDao.listAll();
            int total = allBeds.size();
            int occupied = 0;
            int free = 0;
            int out = 0;
            for (Bed bed : allBeds) {
                if (bed.getBedStatus() == BedStatus.FREE.getCode()) {
                    free++;
                } else if (bed.getBedStatus() == BedStatus.OCCUPIED.getCode()) {
                    occupied++;
                } else if (bed.getBedStatus() == BedStatus.OUT.getCode()) {
                    out++;
                }
            }
            return new int[]{total, occupied, free, out};
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{0, 0, 0, 0};
        }
    }

    /**
     * 换床操作：旧床位→空闲，新床位→有人，同步更新客户床位，结束旧BedDetail并新增。
     * 实现：通过CustomerDao查找占用旧床位的客户，更新其bedId和roomNo；结束旧BedDetail；调更新新旧床位状态。
     */
    public void swapBed(int oldBedId, int newBedId) {
        try {
            Bed oldBed = bedDao.getById(oldBedId);
            Bed newBed = bedDao.getById(newBedId);
            if (oldBed == null || newBed == null) return;

            // 更新床位状态
            oldBed.setBedStatus(BedStatus.FREE.getCode()); // 空闲
            bedDao.update(oldBed);
            newBed.setBedStatus(BedStatus.OCCUPIED.getCode()); // 有人
            bedDao.update(newBed);

            // 查找占用旧床位的客户，同步更新
            CustomerDao customerDao = new CustomerDao();
            BedDetailService bds = new BedDetailService();
            String today = LocalDate.now().toString();
            List<Customer> allCustomers = customerDao.listAll();
            for (Customer c : allCustomers) {
                if (c.getBedId() == oldBedId && c.getIsDeleted() == 0) {
                    // 结束旧床位详情
                    BedDetail oldDetail = bds.getCurrentByCustomerId(c.getId());
                    if (oldDetail != null) {
                        bds.endBedDetail(oldDetail.getId());
                    }
                    // 更新客户床位
                    c.setBedId(newBedId);
                    c.setRoomNo(String.valueOf(newBed.getRoomNo()));
                    customerDao.update(c);
                    // 新增床位详情
                    BedDetail newDetail = new BedDetail();
                    newDetail.setCustomerId(c.getId());
                    newDetail.setBedId(newBedId);
                    newDetail.setStartDate(today);
                    newDetail.setRemark("床位调换，从" + oldBed.getBedNo() + "换至" + newBed.getBedNo());
                    newDetail.setIsDeleted(0);
                    bds.addBedDetail(newDetail);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有房间列表（从床位数据提取不重复房间号，自动推导楼层）。
     * 实现：调用bedDao.listAll遍历去重，按房间号前缀推导楼层。
     */
    public List<Room> listAllRooms() {
        List<Bed> beds = bedDao.listAll();
        Set<Integer> roomNoSet = new HashSet<>();
        List<Room> rooms = new ArrayList<>();
        for (Bed bed : beds) {
            if (!roomNoSet.contains(bed.getRoomNo())) {
                roomNoSet.add(bed.getRoomNo());
                String floor = (bed.getRoomNo() / 100) + "楼";
                rooms.add(new Room(bed.getRoomNo(), floor, bed.getRoomNo()));
            }
        }
        return rooms;
    }
}
