package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.NurseLevelItem;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NurseLevelItemDao {

    private static final String DATA_FILE = "nurselevelitem.json";

    /**
     * 按等级ID查询关联的护理项目列表。
     * 实现：过滤nurselevelitem.json中levelId匹配的记录后返回。
     */
    public List<NurseLevelItem> listByLevelId(int levelId) {
        try {
            List<NurseLevelItem> list = JsonUtil.readList(DATA_FILE, NurseLevelItem.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list.stream()
                    .filter(item -> item.getLevelId() == levelId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 为护理等级添加项目关联，自动生成ID并去重。
     * 实现：调用IdGenerator获取自增ID，检查levelId+itemId不重复后追加写回。
     */
    public void addItem(int levelId, int itemId) {
        try {
            List<NurseLevelItem> list = JsonUtil.readList(DATA_FILE, NurseLevelItem.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            // check duplicate
            for (NurseLevelItem item : list) {
                if (item.getLevelId() == levelId && item.getItemId() == itemId) {
                    return; // already exists
                }
            }
            int nextId = IdGenerator.nextId(DATA_FILE);
            NurseLevelItem newItem = new NurseLevelItem();
            newItem.setId(nextId);
            newItem.setLevelId(levelId);
            newItem.setItemId(itemId);
            list.add(newItem);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按等级和项目删除单条关联记录。
     * 实现：移除列表中levelId和itemId同时匹配的记录后写回。
     */
    public void removeItem(int levelId, int itemId) {
        try {
            List<NurseLevelItem> list = JsonUtil.readList(DATA_FILE, NurseLevelItem.class);
            if (list == null) {
                return;
            }
            list.removeIf(item -> item.getLevelId() == levelId && item.getItemId() == itemId);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按项目ID删除所有等级关联记录。
     * 实现：移除列表中itemId匹配的全部记录后写回。
     */
    public void removeAllByItemId(int itemId) {
        try {
            List<NurseLevelItem> list = JsonUtil.readList(DATA_FILE, NurseLevelItem.class);
            if (list == null) {
                return;
            }
            list.removeIf(item -> item.getItemId() == itemId);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有等级项目关联记录。
     * 实现：读取nurselevelitem.json全量数据后返回。
     */
    public List<NurseLevelItem> listAll() {
        try {
            List<NurseLevelItem> list = JsonUtil.readList(DATA_FILE, NurseLevelItem.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
