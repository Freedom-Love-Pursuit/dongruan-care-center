package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Backdown;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BackdownDao {

    private static final String DATA_FILE = "backdown.json";
    private static final String CUSTOMER_FILE = "customer.json";

    /**
     * 查询所有退住记录列表。
     * 实现：读取backdown.json全量数据后返回。
     */
    public List<Backdown> listAll() {
        try {
            List<Backdown> list = JsonUtil.readList(DATA_FILE, Backdown.class);
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
     * 按ID查询单条退住记录。
     * 实现：遍历backdown.json列表，匹配id后返回。
     */
    public Backdown getById(int id) {
        try {
            List<Backdown> list = JsonUtil.readList(DATA_FILE, Backdown.class);
            if (list == null) {
                return null;
            }
            for (Backdown b : list) {
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
     * 新增退住记录，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回backdown.json。
     */
    public void add(Backdown backdown) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            backdown.setId(nextId);
            List<Backdown> list = JsonUtil.readList(DATA_FILE, Backdown.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(backdown);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新退住记录。
     * 实现：遍历列表匹配id，替换后写回backdown.json。
     */
    public void update(Backdown backdown) {
        try {
            List<Backdown> list = JsonUtil.readList(DATA_FILE, Backdown.class);
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == backdown.getId()) {
                    list.set(i, backdown);
                    JsonUtil.writeList(DATA_FILE, list);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户姓名模糊搜索退住记录。
     * 实现：先从customer.json模糊匹配客户姓名获取客户ID列表，再过滤backdown.json中匹配的记录。
     */
    public List<Backdown> searchByCustomerName(String keyword) {
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
            // Filter backdown records by matching customer IDs
            List<Backdown> backdowns = JsonUtil.readList(DATA_FILE, Backdown.class);
            if (backdowns == null) {
                return new ArrayList<>();
            }
            return backdowns.stream()
                    .filter(b -> matchingCustomerIds.contains(b.getCustomerId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
