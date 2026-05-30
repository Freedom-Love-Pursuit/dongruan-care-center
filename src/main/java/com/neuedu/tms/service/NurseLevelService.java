package com.neuedu.tms.service;

import com.neuedu.tms.dao.NurseLevelDao;
import com.neuedu.tms.pojo.NurseLevel;

import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class NurseLevelService {

    private NurseLevelDao nurseLevelDao = new NurseLevelDao();

    /**
     * 查询所有护理等级。
     * 实现：调用nurseLevelDao.listAll查询全部，多条时按等级名称排序。
     */
    public List<NurseLevel> listAll() {
        try {
            List<NurseLevel> levels = nurseLevelDao.listAll();
            if (levels != null && levels.size() > 1) {
                Collections.sort(levels, SortUtil.BY_LEVEL_NAME);
            }
            return levels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询护理等级。
     * 实现：调用nurseLevelDao.getById按ID查询并返回。
     */
    public NurseLevel getById(int id) {
        try {
            return nurseLevelDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增护理等级。
     * 实现：调用nurseLevelDao.add写入护理等级记录。
     */
    public void addNurseLevel(NurseLevel nurseLevel) {
        try {
            nurseLevelDao.add(nurseLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新护理等级。
     * 实现：调用nurseLevelDao.update更新护理等级记录。
     */
    public void updateNurseLevel(NurseLevel nurseLevel) {
        try {
            nurseLevelDao.update(nurseLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除护理等级。
     * 实现：先调nurseLevelDao.getById获取，设isDeleted=1后调nurseLevelDao.update更新。
     */
    public void deleteNurseLevel(int id) {
        try {
            NurseLevel nurseLevel = nurseLevelDao.getById(id);
            if (nurseLevel != null) {
                nurseLevel.setIsDeleted(1);
                nurseLevelDao.update(nurseLevel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有启用状态的护理等级。
     * 实现：调用nurseLevelDao.listActive查询启用等级，多条时按等级名称排序。
     */
    public List<NurseLevel> listActive() {
        try {
            List<NurseLevel> levels = nurseLevelDao.listActive();
            if (levels != null && levels.size() > 1) {
                Collections.sort(levels, SortUtil.BY_LEVEL_NAME);
            }
            return levels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置护理等级状态。
     * 实现：先调nurseLevelDao.getById获取，设新状态后调nurseLevelDao.update更新。
     */
    public void setLevelStatus(int id, int status) {
        try {
            NurseLevel nurseLevel = nurseLevelDao.getById(id);
            if (nurseLevel != null) {
                nurseLevel.setLevelStatus(status);
                nurseLevelDao.update(nurseLevel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据状态搜索护理等级，status为null时查全部。
     * 实现：调用nurseLevelDao.listAll或searchByStatus查询，多条时按等级名称排序。
     */
    public List<NurseLevel> searchByStatus(Integer status) {
        try {
            List<NurseLevel> levels;
            if (status == null) {
                levels = nurseLevelDao.listAll();
            } else {
                levels = nurseLevelDao.searchByStatus(status);
            }
            if (levels != null && levels.size() > 1) {
                Collections.sort(levels, SortUtil.BY_LEVEL_NAME);
            }
            return levels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
