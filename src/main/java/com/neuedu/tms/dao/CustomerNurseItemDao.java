package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.CustomerNurseItem;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerNurseItemDao {

    private static final String DATA_FILE = "customernurseitem.json";

    /**
     * 按客户ID查询未删除的护理项目列表。
     * 实现：过滤customernurseitem.json中customerId匹配且isDeleted==0的记录。
     */
    public List<CustomerNurseItem> listByCustomerId(int customerId) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return new ArrayList<>();
            }
            return items.stream()
                    .filter(i -> i.getCustomerId() == customerId && i.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 新增单条护理项目关联记录，自动生成ID。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回customernurseitem.json。
     */
    public void add(CustomerNurseItem item) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            item.setId(nextId);
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(item);
            JsonUtil.writeList(DATA_FILE, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量新增护理项目关联记录，每条自动生成ID。
     * 实现：遍历传入列表，逐条调用IdGenerator获取自增ID后追加写回。
     */
    public void batchAdd(List<CustomerNurseItem> items) {
        try {
            if (items == null || items.isEmpty()) {
                return;
            }
            List<CustomerNurseItem> existingItems = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (existingItems == null) {
                existingItems = new ArrayList<>();
            }
            for (CustomerNurseItem item : items) {
                int nextId = IdGenerator.nextId(DATA_FILE);
                item.setId(nextId);
                existingItems.add(item);
            }
            JsonUtil.writeList(DATA_FILE, existingItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除单条护理项目关联记录。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回。
     */
    public void delete(int id) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return;
            }
            for (CustomerNurseItem i : items) {
                if (i.getId() == id) {
                    i.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, items);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户和项目软删除关联记录。
     * 实现：遍历列表匹配customerId和itemId，设置isDeleted=1后写回。
     */
    public void deleteByCustomerAndItem(int customerId, int itemId) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return;
            }
            for (CustomerNurseItem i : items) {
                if (i.getCustomerId() == customerId && i.getItemId() == itemId) {
                    i.setIsDeleted(1);
                }
            }
            JsonUtil.writeList(DATA_FILE, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户ID软删除所有关联记录。
     * 实现：遍历列表匹配customerId，全部设置isDeleted=1后写回。
     */
    public void deleteByCustomerAndLevel(int customerId) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return;
            }
            // Delete all level-related items for this customer
            // Level-related items are those where type indicates a level-level assignment
            for (CustomerNurseItem i : items) {
                if (i.getCustomerId() == customerId) {
                    i.setIsDeleted(1);
                }
            }
            JsonUtil.writeList(DATA_FILE, items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户和项目查询未删除的护理项目关联记录。
     * 实现：遍历列表匹配customerId、itemId且isDeleted==0后返回。
     */
    public CustomerNurseItem getByCustomerAndItem(int customerId, int itemId) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return null;
            }
            for (CustomerNurseItem i : items) {
                if (i.getCustomerId() == customerId && i.getItemId() == itemId && i.getIsDeleted() == 0) {
                    return i;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按ID更新护理项目关联记录。
     * 实现：遍历列表匹配id，替换后写回customernurseitem.json。
     */
    public void update(CustomerNurseItem item) {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return;
            }
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId() == item.getId()) {
                    items.set(i, item);
                    JsonUtil.writeList(DATA_FILE, items);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有护理项目关联记录。
     * 实现：读取customernurseitem.json全量数据后返回。
     */
    public List<CustomerNurseItem> listAll() {
        try {
            List<CustomerNurseItem> items = JsonUtil.readList(DATA_FILE, CustomerNurseItem.class);
            if (items == null) {
                return new ArrayList<>();
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
