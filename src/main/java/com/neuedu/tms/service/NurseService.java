package com.neuedu.tms.service;

import com.neuedu.tms.pojo.CustomerNurseItem;
import com.neuedu.tms.pojo.NurseContent;
import com.neuedu.tms.pojo.NurseLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

/**
 * 护理聚合服务：将分散的护理相关服务包装为统一入口，供 ConsoleUI 调用
 */
public class NurseService {

    private NurseContentService ncService = new NurseContentService();
    private NurseLevelService nlService = new NurseLevelService();
    private NurseLevelItemService nliService = new NurseLevelItemService();
    private CustomerNurseItemService cniService = new CustomerNurseItemService();

    // ==================== 护理内容 ====================

    /**
     * 查询所有启用状态的护理项目列表。
     * 实现：委托ncService.listActive查询启用护理内容。
     */
    public List<NurseContent> listNurseItems() {
        return ncService.listActive();
    }

    /**
     * 新增护理项目。
     * 实现：委托ncService.addNurseContent添加，成功返回true。
     */
    public boolean addNurseItem(NurseContent item) {
        try {
            ncService.addNurseContent(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据ID查找护理项目。
     * 实现：委托ncService.getById按ID查询并返回。
     */
    public NurseContent findNurseItemById(int id) {
        return ncService.getById(id);
    }

    /**
     * 更新护理项目。
     * 实现：委托ncService.updateNurseContent更新，成功返回true。
     */
    public boolean updateNurseItem(NurseContent item) {
        try {
            ncService.updateNurseContent(item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除护理项目。
     * 实现：委托ncService.deleteNurseContent删除，成功返回true。
     */
    public boolean deleteNurseItem(int id) {
        try {
            ncService.deleteNurseContent(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== 护理等级 ====================

    /**
     * 查询所有启用状态的护理等级列表。
     * 实现：委托nlService.listActive查询启用等级。
     */
    public List<NurseLevel> listNurseLevels() {
        return nlService.listActive();
    }

    /**
     * 新增护理等级。
     * 实现：委托nlService.addNurseLevel添加，成功返回true。
     */
    public boolean addNurseLevel(NurseLevel level) {
        try {
            nlService.addNurseLevel(level);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据ID查找护理等级。
     * 实现：委托nlService.getById按ID查询并返回。
     */
    public NurseLevel findNurseLevelById(int id) {
        return nlService.getById(id);
    }

    /**
     * 更新护理等级。
     * 实现：委托nlService.updateNurseLevel更新，成功返回true。
     */
    public boolean updateNurseLevel(NurseLevel level) {
        try {
            nlService.updateNurseLevel(level);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除护理等级。
     * 实现：委托nlService.deleteNurseLevel删除，成功返回true。
     */
    public boolean deleteNurseLevel(int id) {
        try {
            nlService.deleteNurseLevel(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== 客户护理项目 ====================

    /**
     * 获取客户已购买的护理项目列表（以NurseContent形式返回）。
     * 实现：委托cniService.listByCustomerId获取关联记录，逐项转换为NurseContent后按序号排序返回。
     */
    public List<NurseContent> getCustomerNurseItems(int customerId) {
        List<CustomerNurseItem> items = cniService.listByCustomerId(customerId);
        List<NurseContent> contents = new ArrayList<>();
        for (CustomerNurseItem item : items) {
            NurseContent nc = ncService.getById(item.getItemId());
            if (nc != null) {
                contents.add(nc);
            }
        }
        if (contents.size() > 1) {
            Collections.sort(contents, SortUtil.BY_SERIAL_NUMBER);
        }
        return contents;
    }

    /**
     * 为客户设置护理项目（购买）。
     * 实现：委托cniService.buyItems批量购买，成功返回true。
     */
    public boolean setCustomerNurseItems(int customerId, List<Integer> itemIds) {
        try {
            cniService.buyItems(customerId, itemIds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
