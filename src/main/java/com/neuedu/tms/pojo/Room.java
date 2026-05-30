package com.neuedu.tms.pojo;

/**
 * 房间实体类，记录颐养中心各房间信息。
 * 字段：房间号、楼层等。
 */
public class Room {

    private int id;
    private String roomFloor;
    private int roomNo;

    public Room() {
    }

    public Room(int id, String roomFloor, int roomNo) {
        this.id = id;
        this.roomFloor = roomFloor;
        this.roomNo = roomNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        this.roomFloor = roomFloor;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomFloor='" + roomFloor + '\'' +
                ", roomNo=" + roomNo +
                '}';
    }
}
