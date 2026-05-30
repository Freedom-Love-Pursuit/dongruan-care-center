package com.neuedu.tms.service;

import com.neuedu.tms.dao.NurseRecordDao;
import com.neuedu.tms.dao.CustomerNurseItemDao;
import com.neuedu.tms.pojo.NurseRecord;
import com.neuedu.tms.pojo.CustomerNurseItem;

import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class NurseRecordService {

    private NurseRecordDao nurseRecordDao = new NurseRecordDao();
    private CustomerNurseItemDao customerNurseItemDao = new CustomerNurseItemDao();

    /**
     * 新增护理记录，同时扣减客户护理项目的剩余次数。
     * 实现：先调nurseRecordDao.add写入记录，再获取关联的CustomerNurseItem并扣减nurseNumber（剩余次数-已完成次数）。
     */
    public void addRecord(NurseRecord record) {
        try {
            nurseRecordDao.add(record);

            CustomerNurseItem cni = customerNurseItemDao.getByCustomerAndItem(
                    record.getCustomerId(), record.getItemId());
            if (cni != null) {
                int remaining = cni.getNurseNumber() - record.getNursingCount();
                cni.setNurseNumber(Math.max(remaining, 0)); // 扣减，最低为0
                customerNurseItemDao.update(cni);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据客户ID查询护理记录。
     * 实现：调用nurseRecordDao.listByCustomerId查询，多条时按时间降序排序。
     */
    public List<NurseRecord> listByCustomerId(int customerId) {
        try {
            List<NurseRecord> records = nurseRecordDao.listByCustomerId(customerId);
            if (records != null && records.size() > 1) {
                Collections.sort(records, SortUtil.BY_TIME_DESC);
            }
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 逻辑删除护理记录。
     * 实现：调用nurseRecordDao.delete删除记录。
     */
    public void deleteRecord(int id) {
        try {
            nurseRecordDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
