package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.Outward;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutwardDao {

    private static final String DATA_FILE = "outward.json";
    private static final String CUSTOMER_FILE = "customer.json";

    /**
     * 查询所有外出记录列表。
     * 实现：读取outward.json全量数据后返回。
     */
    public List<Outward> listAll() {
        try {
            List<Outward> list = JsonUtil.readList(DATA_FILE, Outward.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询单条外出记录。
     * 实现：遍历outward.json列表，匹配id后返回。
     */
    public Outward getById(int id) {
        try {
            List<Outward> list = JsonUtil.readList(DATA_FILE, Outward.class);
            if (list == null) {
                return null;
            }
            for (Outward o : list) {
                if (o.getId() == id) {
                    return o;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增外出记录，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回outward.json。
     */
    public void add(Outward outward) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            outward.setId(nextId);
            List<Outward> list = JsonUtil.readList(DATA_FILE, Outward.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(outward);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新外出记录。
     * 实现：遍历列表匹配id，替换后写回outward.json。
     */
    public void update(Outward outward) {
        try {
            List<Outward> list = JsonUtil.readList(DATA_FILE, Outward.class);
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == outward.getId()) {
                    list.set(i, outward);
                    JsonUtil.writeList(DATA_FILE, list);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户姓名模糊搜索外出记录。
     * 实现：先从customer.json匹配客户姓名获取ID列表，再过滤outward.json中匹配的记录。
     */
    public List<Outward> searchByCustomerName(String keyword) {
        try {
            // Load customers first to find matching customer IDs
            List<Customer> customers = JsonUtil.readList(CUSTOMER_FILE, Customer.class);
            List<Integer> matchingCustomerIds = new ArrayList<>();
            if (customers != null) {
                String lowerKeyword = keyword.toLowerCase();
                for (Customer c : customers) {
                    if (c.getCustomerName() != null
                            && c.getCustomerName().toLowerCase().contains(lowerKeyword)) {
                        matchingCustomerIds.add(c.getId());
                    }
                }
            }
            // Filter outward records by matching customer IDs
            List<Outward> outwards = JsonUtil.readList(DATA_FILE, Outward.class);
            if (outwards == null) {
                return new ArrayList<>();
            }
            return outwards.stream()
                    .filter(o -> matchingCustomerIds.contains(o.getCustomerId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
