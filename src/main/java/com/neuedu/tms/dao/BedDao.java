package com.neuedu.tms.dao;

import com.neuedu.tms.enums.BedStatus;
import com.neuedu.tms.pojo.Bed;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BedDao {

    private static final String DATA_FILE = "bed.json";

    /**
     * 查询所有床位列表。
     * 实现：读取bed.json全量数据后返回。
     */
    public List<Bed> listAll() {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return new ArrayList<>();
            }
            return beds;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询单条床位记录。
     * 实现：遍历bed.json列表，匹配id后返回。
     */
    public Bed getById(int id) {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return null;
            }
            for (Bed b : beds) {
                if (b.getId() == id) {
                    return b;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增床位，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回bed.json。
     */
    public void add(Bed bed) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            bed.setId(nextId);
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                beds = new ArrayList<>();
            }
            beds.add(bed);
            JsonUtil.writeList(DATA_FILE, beds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新床位信息。
     * 实现：遍历列表匹配id，替换后写回bed.json。
     */
    public void update(Bed bed) {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return;
            }
            for (int i = 0; i < beds.size(); i++) {
                if (beds.get(i).getId() == bed.getId()) {
                    beds.set(i, bed);
                    JsonUtil.writeList(DATA_FILE, beds);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新床位状态。
     * 实现：遍历列表匹配id，设置bedStatus后写回bed.json。
     */
    public void updateStatus(int id, int status) {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return;
            }
            for (Bed b : beds) {
                if (b.getId() == id) {
                    b.setBedStatus(status);
                    JsonUtil.writeList(DATA_FILE, beds);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按房间号查询床位列表。
     * 实现：过滤bed.json中roomNo匹配的记录后返回。
     */
    public List<Bed> listByRoomNo(int roomNo) {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return new ArrayList<>();
            }
            return beds.stream()
                    .filter(b -> b.getRoomNo() == roomNo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按房间号查询空闲床位列表。
     * 实现：过滤roomNo匹配且bedStatus==1的记录后返回。
     */
    public List<Bed> listAvailableByRoomNo(int roomNo) {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return new ArrayList<>();
            }
            return beds.stream()
                    .filter(b -> b.getRoomNo() == roomNo && b.getBedStatus() == BedStatus.FREE.getCode())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 统计床位使用情况，返回{总数,空闲,占用,外出}。
     * 实现：遍历bed.json，按bedStatus分别统计各状态数量。
     */
    public int[] getStats() {
        try {
            List<Bed> beds = JsonUtil.readList(DATA_FILE, Bed.class);
            if (beds == null) {
                return new int[]{0, 0, 0, 0};
            }
            int total = beds.size();
            int free = 0;
            int occupied = 0;
            int out = 0;
            for (Bed b : beds) {
                if (b.getBedStatus() == BedStatus.FREE.getCode()) {
                    free++;
                } else if (b.getBedStatus() == BedStatus.OCCUPIED.getCode()) {
                    occupied++;
                } else if (b.getBedStatus() == BedStatus.OUT.getCode()) {
                    out++;
                }
            }
            return new int[]{total, free, occupied, out};
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{0, 0, 0, 0};
        }
    }
}
