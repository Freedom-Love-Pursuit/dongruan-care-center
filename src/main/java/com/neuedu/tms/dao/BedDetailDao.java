package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.BedDetail;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BedDetailDao {

    private static final String DATA_FILE = "beddetail.json";

    /**
     * 按客户ID查询未删除的床位明细列表。
     * 实现：过滤beddetail.json中customerId匹配且isDeleted==0的记录。
     */
    public List<BedDetail> listByCustomerId(int customerId) {
        try {
            List<BedDetail> details = JsonUtil.readList(DATA_FILE, BedDetail.class);
            if (details == null) {
                return new ArrayList<>();
            }
            return details.stream()
                    .filter(d -> d.getIsDeleted() == 0 && d.getCustomerId() == customerId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按客户ID查询当前有效的床位明细（endDate为空的记录）。
     * 实现：遍历beddetail.json，匹配customerId且endDate==null且isDeleted==0后返回。
     */
    public BedDetail getCurrentByCustomerId(int customerId) {
        try {
            List<BedDetail> details = JsonUtil.readList(DATA_FILE, BedDetail.class);
            if (details == null) {
                return null;
            }
            for (BedDetail d : details) {
                if (d.getIsDeleted() == 0
                        && d.getCustomerId() == customerId
                        && d.getEndDate() == null) {
                    return d;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增床位明细，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回beddetail.json。
     */
    public void add(BedDetail bd) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            bd.setId(nextId);
            List<BedDetail> details = JsonUtil.readList(DATA_FILE, BedDetail.class);
            if (details == null) {
                details = new ArrayList<>();
            }
            details.add(bd);
            JsonUtil.writeList(DATA_FILE, details);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新床位明细记录。
     * 实现：遍历列表匹配id，替换后写回beddetail.json。
     */
    public void update(BedDetail bd) {
        try {
            List<BedDetail> details = JsonUtil.readList(DATA_FILE, BedDetail.class);
            if (details == null) {
                return;
            }
            for (int i = 0; i < details.size(); i++) {
                if (details.get(i).getId() == bd.getId()) {
                    details.set(i, bd);
                    JsonUtil.writeList(DATA_FILE, details);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束床位明细记录，设置结束日期并标记已删除。
     * 实现：遍历列表匹配id，设置endDate和isDeleted=1后写回beddetail.json。
     */
    public void endBedDetail(int id, String endDate) {
        try {
            List<BedDetail> details = JsonUtil.readList(DATA_FILE, BedDetail.class);
            if (details == null) {
                return;
            }
            for (BedDetail d : details) {
                if (d.getId() == id) {
                    d.setEndDate(endDate);
                    d.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, details);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
