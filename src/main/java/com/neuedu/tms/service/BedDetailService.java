package com.neuedu.tms.service;

import com.neuedu.tms.dao.BedDetailDao;
import com.neuedu.tms.pojo.BedDetail;

import java.time.LocalDate;
import java.util.List;

/**
 * 床位详情记录管理服务
 */
public class BedDetailService {

    private BedDetailDao bedDetailDao = new BedDetailDao();

    /**
     * 根据客户ID查询所有床位详情记录。
     * 实现：调用bedDetailDao.listByCustomerId按客户ID查询并返回。
     */
    public List<BedDetail> listByCustomerId(int customerId) {
        try {
            return bedDetailDao.listByCustomerId(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据客户ID查询当前有效的床位详情。
     * 实现：调用bedDetailDao.getCurrentByCustomerId查询当前有效记录并返回。
     */
    public BedDetail getCurrentByCustomerId(int customerId) {
        try {
            return bedDetailDao.getCurrentByCustomerId(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增床位详情记录。
     * 实现：调用bedDetailDao.add写入床位详情记录。
     */
    public void addBedDetail(BedDetail bd) {
        try {
            bedDetailDao.add(bd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束床位详情记录，设置结束日期为当天。
     * 实现：调用bedDetailDao.endBedDetail以当天日期结束记录。
     */
    public void endBedDetail(int id) {
        try {
            bedDetailDao.endBedDetail(id, LocalDate.now().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
