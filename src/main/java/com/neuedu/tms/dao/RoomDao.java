package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Room;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDao {

    private static final String DATA_FILE = "room.json";

    /**
     * 查询所有房间列表。
     * 实现：读取room.json全量数据后返回。
     */
    public List<Room> listAll() {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return new ArrayList<>();
            }
            return rooms;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按楼层查询房间列表。
     * 实现：过滤room.json中roomFloor匹配的记录后返回。
     */
    public List<Room> listByFloor(String floor) {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return new ArrayList<>();
            }
            return rooms.stream()
                    .filter(r -> r.getRoomFloor() != null && r.getRoomFloor().equals(floor))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 查询所有楼层列表（去重）。
     * 实现：读取room.json，提取所有非空roomFloor并去重后返回。
     */
    public List<String> listFloors() {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return new ArrayList<>();
            }
            return rooms.stream()
                    .map(Room::getRoomFloor)
                    .filter(floor -> floor != null)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询房间记录。
     * 实现：遍历room.json列表，匹配id后返回。
     */
    public Room getById(int id) {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return null;
            }
            for (Room r : rooms) {
                if (r.getId() == id) {
                    return r;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增房间，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回room.json。
     */
    public void add(Room room) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            room.setId(nextId);
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                rooms = new ArrayList<>();
            }
            rooms.add(room);
            JsonUtil.writeList(DATA_FILE, rooms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新房间信息。
     * 实现：遍历列表匹配id，替换后写回room.json。
     */
    public void update(Room room) {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return;
            }
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).getId() == room.getId()) {
                    rooms.set(i, room);
                    JsonUtil.writeList(DATA_FILE, rooms);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID物理删除房间记录。
     * 实现：移除列表中id匹配的记录后写回room.json。
     */
    public void delete(int id) {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return;
            }
            rooms.removeIf(r -> r.getId() == id);
            JsonUtil.writeList(DATA_FILE, rooms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查房间号是否已存在。
     * 实现：遍历room.json，匹配roomNo后返回布尔。
     */
    public boolean existsByRoomNo(int roomNo) {
        try {
            List<Room> rooms = JsonUtil.readList(DATA_FILE, Room.class);
            if (rooms == null) {
                return false;
            }
            for (Room r : rooms) {
                if (r.getRoomNo() == roomNo) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
