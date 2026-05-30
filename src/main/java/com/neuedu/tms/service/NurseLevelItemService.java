package com.neuedu.tms.service;

import com.neuedu.tms.dao.NurseLevelItemDao;
import com.neuedu.tms.dao.NurseContentDao;
import com.neuedu.tms.pojo.NurseLevelItem;
import com.neuedu.tms.pojo.NurseContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class NurseLevelItemService {

    private NurseLevelItemDao nurseLevelItemDao = new NurseLevelItemDao();
    private NurseContentDao nurseContentDao = new NurseContentDao();

    /**
     * 向护理等级中添加护理项目（去重校验）。
     * 实现：先调nurseLevelItemDao.listByLevelId检查是否已存在，不重复则调addItem添加。
     */
    public void addItem(int levelId, int itemId) {
        try {
            // 检查是否已存在
            List<NurseLevelItem> existingItems = nurseLevelItemDao.listByLevelId(levelId);
            for (NurseLevelItem li : existingItems) {
                if (li.getItemId() == itemId) {
                    System.out.println("该项目已在等级中，不可重复添加");
                    return;
                }
            }
            nurseLevelItemDao.addItem(levelId, itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从护理等级中移除护理项目。
     * 实现：调用nurseLevelItemDao.removeItem删除指定等级下的指定项目。
     */
    public void removeItem(int levelId, int itemId) {
        try {
            nurseLevelItemDao.removeItem(levelId, itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询指定护理等级下的所有护理项目。
     * 实现：调用nurseLevelItemDao.listByLevelId查询，多条时按项目ID排序。
     */
    public List<NurseLevelItem> listByLevelId(int levelId) {
        try {
            List<NurseLevelItem> items = nurseLevelItemDao.listByLevelId(levelId);
            if (items != null && items.size() > 1) {
                Collections.sort(items, SortUtil.BY_ITEM_ID);
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取未加入指定等级的可用护理项目（启用状态且未关联）。
     * 实现：调nurseContentDao.listActive获取所有启用项目，再调nurseLevelItemDao.listByLevelId获取已有关联，筛选出差集后按序号排序返回。
     */
    public List<NurseContent> getAvailableItems(int levelId) {
        try {
            // 查询所有启用的护理内容
            List<NurseContent> allActive = nurseContentDao.listActive();
            // 查询当前等级已有的项目
            List<NurseLevelItem> levelItems = nurseLevelItemDao.listByLevelId(levelId);

            // 收集已有的itemId
            List<Integer> existingItemIds = new ArrayList<>();
            for (NurseLevelItem li : levelItems) {
                existingItemIds.add(li.getItemId());
            }

            // 筛选出未加入该等级的项目
            List<NurseContent> available = new ArrayList<>();
            for (NurseContent nc : allActive) {
                if (!existingItemIds.contains(nc.getId())) {
                    available.add(nc);
                }
            }
            if (available != null && available.size() > 1) {
                Collections.sort(available, SortUtil.BY_SERIAL_NUMBER);
            }
            return available;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
