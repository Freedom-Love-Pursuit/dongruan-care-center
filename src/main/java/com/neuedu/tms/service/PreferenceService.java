package com.neuedu.tms.service;

import com.neuedu.tms.dao.PreferenceDao;
import com.neuedu.tms.pojo.Preference;

/**
 * 客户偏好管理服务
 */
public class PreferenceService {

    private PreferenceDao preferenceDao = new PreferenceDao();

    /**
     * 根据客户ID查询偏好信息。
     * 实现：调用preferenceDao.getByCustomerId按客户ID查询并返回。
     */
    public Preference getByCustomerId(int customerId) {
        try {
            return preferenceDao.getByCustomerId(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存或更新偏好（存在则更新，不存在则新增）。
     * 实现：先调preferenceDao.existsByCustomerId判断，存在则调update，否则调add写入。
     */
    public boolean saveOrUpdate(Preference p) {
        try {
            if (preferenceDao.existsByCustomerId(p.getCustomerId())) {
                preferenceDao.update(p);
            } else {
                preferenceDao.add(p);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
