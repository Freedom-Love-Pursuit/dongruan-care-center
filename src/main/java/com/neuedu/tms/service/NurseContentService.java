package com.neuedu.tms.service;

import com.neuedu.tms.dao.NurseContentDao;
import com.neuedu.tms.dao.NurseLevelItemDao;
import com.neuedu.tms.pojo.NurseContent;

import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class NurseContentService {

    private NurseContentDao nurseContentDao = new NurseContentDao();
    private NurseLevelItemDao nurseLevelItemDao = new NurseLevelItemDao();

    /**
     * 查询所有未删除的护理内容。
     * 实现：调用nurseContentDao.listAll查询全部，多条时按序号排序。
     */
    public List<NurseContent> listAll() {
        try {
            List<NurseContent> contents = nurseContentDao.listAll();
            if (contents != null && contents.size() > 1) {
                Collections.sort(contents, SortUtil.BY_SERIAL_NUMBER);
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询护理内容。
     * 实现：调用nurseContentDao.getById按ID查询并返回。
     */
    public NurseContent getById(int id) {
        try {
            return nurseContentDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增护理内容，自动生成编号NC+6位数字。
     * 实现：先调nurseContentDao.add写入，获取自增ID后生成流水号，再调nurseContentDao.update回写编号。
     */
    public void addNurseContent(NurseContent nurseContent) {
        try {
            nurseContentDao.add(nurseContent);
            String serialNumber = "NC" + String.format("%06d", nurseContent.getId());
            nurseContent.setSerialNumber(serialNumber);
            nurseContentDao.update(nurseContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新护理内容，停用时自动从所有等级项目中移除。
     * 实现：先调nurseContentDao.update更新，若状态为停用(2)则调nurseLevelItemDao.removeAllByItemId移除关联。
     */
    public void updateNurseContent(NurseContent nurseContent) {
        try {
            nurseContentDao.update(nurseContent);
            if (nurseContent.getStatus() == 2) {
                // 状态变为停用，从所有护理等级项目中移除该项
                nurseLevelItemDao.removeAllByItemId(nurseContent.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除护理内容，同时从等级项目中移除。
     * 实现：先调nurseContentDao.getById获取后设isDeleted=1，再调nurseContentDao.update更新，最后调nurseLevelItemDao.removeAllByItemId移除关联。
     */
    public void deleteNurseContent(int id) {
        try {
            NurseContent nurseContent = nurseContentDao.getById(id);
            if (nurseContent != null) {
                nurseContent.setIsDeleted(1);
                nurseContentDao.update(nurseContent);
                // 从所有护理等级项目中移除该项
                nurseLevelItemDao.removeAllByItemId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据关键字和状态搜索护理内容。
     * 实现：调用nurseContentDao.search按关键字和状态筛选，多条时按序号排序。
     */
    public List<NurseContent> search(String keyword, Integer status) {
        try {
            List<NurseContent> contents = nurseContentDao.search(keyword, status);
            if (contents != null && contents.size() > 1) {
                Collections.sort(contents, SortUtil.BY_SERIAL_NUMBER);
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有启用状态的护理内容。
     * 实现：调用nurseContentDao.listActive查询启用项目，多条时按序号排序。
     */
    public List<NurseContent> listActive() {
        try {
            List<NurseContent> contents = nurseContentDao.listActive();
            if (contents != null && contents.size() > 1) {
                Collections.sort(contents, SortUtil.BY_SERIAL_NUMBER);
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
