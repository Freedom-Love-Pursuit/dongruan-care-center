package com.neuedu.tms.service;

import com.neuedu.tms.dao.BackdownDao;
import com.neuedu.tms.dao.BedDao;
import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.enums.AuditStatus;
import com.neuedu.tms.enums.RetreatType;
import com.neuedu.tms.pojo.Backdown;
import com.neuedu.tms.pojo.Bed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class BackdownService {

    private BackdownDao backdownDao = new BackdownDao();
    private BedDao bedDao = new BedDao();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 查询所有退住记录。
     * 实现：调用backdownDao.listAll查询全部，多条时按退住日期降序排序。
     */
    public List<Backdown> listAll() {
        try {
            List<Backdown> backdowns = backdownDao.listAll();
            if (backdowns != null && backdowns.size() > 1) {
                Collections.sort(backdowns, SortUtil.BACKDOWN_SORT);
            }
            return backdowns;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询退住记录。
     * 实现：调用backdownDao.getById按ID查询并返回。
     */
    public Backdown getById(int id) {
        try {
            return backdownDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增退住记录。
     * 实现：调用backdownDao.add写入退住记录。
     */
    public void addBackdown(Backdown backdown) {
        try {
            backdownDao.add(backdown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新退住记录。
     * 实现：调用backdownDao.update更新退住记录。
     */
    public void updateBackdown(Backdown backdown) {
        try {
            backdownDao.update(backdown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除退住记录。
     * 实现：先调backdownDao.getById获取，设isDeleted=1后调backdownDao.update更新。
     */
    public void deleteBackdown(int id) {
        try {
            Backdown backdown = backdownDao.getById(id);
            if (backdown != null) {
                backdown.setIsDeleted(1);
                backdownDao.update(backdown);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核退住记录，通过后根据退住类型释放床位、结束床位详情。
     * 实现：先更新审核状态；若通过(1)：正常退住(0)或死亡退住(1)释放床位为空闲+结束BedDetail；保留床位(2)不释放。
     */
    public void auditBackdown(int id, int auditStatus, String auditPerson) {
        try {
            Backdown backdown = backdownDao.getById(id);
            if (backdown == null) {
                System.out.println("退住记录不存在");
                return;
            }

            String today = LocalDate.now().format(DATE_FORMATTER);
            backdown.setAuditStatus(auditStatus);
            backdown.setAuditPerson(auditPerson);
            backdown.setAuditTime(today);
            backdownDao.update(backdown);

            if (auditStatus == AuditStatus.APPROVED.getCode()) {
                // 审核通过，根据退住类型处理
                CustomerDao customerDao = new CustomerDao();
                CustomerService cs = new CustomerService();
                if (backdown.getRetreatType() == RetreatType.NORMAL.getCode() || backdown.getRetreatType() == RetreatType.DECEASED.getCode()) {
                    // 正常退住或死亡退住：释放床位+结束BedDetail
                    cs.deleteCustomer(backdown.getCustomerId());
                }
                // 保留床位(2)不释放
                System.out.println("退住审核通过，客户ID: " + backdown.getCustomerId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
