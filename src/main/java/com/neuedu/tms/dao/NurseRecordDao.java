package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.NurseRecord;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NurseRecordDao {

    private static final String DATA_FILE = "nurserecord.json";

    /**
     * 按客户ID查询未删除的护理记录列表。
     * 实现：过滤nurserecord.json中customerId匹配且isDeleted==0的记录。
     */
    public List<NurseRecord> listByCustomerId(int customerId) {
        try {
            List<NurseRecord> records = JsonUtil.readList(DATA_FILE, NurseRecord.class);
            if (records == null) {
                return new ArrayList<>();
            }
            return records.stream()
                    .filter(r -> r.getCustomerId() == customerId && r.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增护理记录，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回nurserecord.json。
     */
    public void add(NurseRecord record) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            record.setId(nextId);
            List<NurseRecord> records = JsonUtil.readList(DATA_FILE, NurseRecord.class);
            if (records == null) {
                records = new ArrayList<>();
            }
            records.add(record);
            JsonUtil.writeList(DATA_FILE, records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除护理记录。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回nurserecord.json。
     */
    public void delete(int id) {
        try {
            List<NurseRecord> records = JsonUtil.readList(DATA_FILE, NurseRecord.class);
            if (records == null) {
                return;
            }
            for (NurseRecord r : records) {
                if (r.getId() == id) {
                    r.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, records);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有护理记录列表。
     * 实现：读取nurserecord.json全量数据后返回。
     */
    public List<NurseRecord> listAll() {
        try {
            List<NurseRecord> records = JsonUtil.readList(DATA_FILE, NurseRecord.class);
            if (records == null) {
                return new ArrayList<>();
            }
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
