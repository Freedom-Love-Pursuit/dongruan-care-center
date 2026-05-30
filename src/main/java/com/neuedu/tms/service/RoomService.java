package com.neuedu.tms.service;

import com.neuedu.tms.dao.RoomDao;
import com.neuedu.tms.pojo.Room;

import java.util.List;

/**
 * 房间管理服务
 */
public class RoomService {

    private RoomDao roomDao = new RoomDao();

    /**
     * 查询所有房间。
     * 实现：调用roomDao.listAll查询全部房间。
     */
    public List<Room> listAll() {
        try {
            return roomDao.listAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据楼层查询房间。
     * 实现：调用roomDao.listByFloor按楼层查询并返回。
     */
    public List<Room> listByFloor(String floor) {
        try {
            return roomDao.listByFloor(floor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有楼层列表（去重）。
     * 实现：调用roomDao.listFloors查询并返回不重复的楼层列表。
     */
    public List<String> listFloors() {
        try {
            return roomDao.listFloors();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增房间，房号重复检测。
     * 实现：先调roomDao.existsByRoomNo检查重复，不重复则调roomDao.add写入。
     */
    public boolean addRoom(Room room) {
        try {
            if (roomDao.existsByRoomNo(room.getRoomNo())) {
                System.out.println("房间号 '" + room.getRoomNo() + "' 已存在，新增失败！");
                return false;
            }
            roomDao.add(room);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新房间信息。
     * 实现：调用roomDao.update更新房间记录。
     */
    public boolean updateRoom(Room room) {
        try {
            roomDao.update(room);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除房间。
     * 实现：调用roomDao.delete删除房间记录。
     */
    public boolean deleteRoom(int id) {
        try {
            roomDao.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
