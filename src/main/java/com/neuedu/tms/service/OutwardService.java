package com.neuedu.tms.service;

import com.neuedu.tms.dao.OutwardDao;
import com.neuedu.tms.dao.BedDao;
import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.enums.AuditStatus;
import com.neuedu.tms.enums.BedStatus;
import com.neuedu.tms.pojo.Outward;
import com.neuedu.tms.pojo.Bed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class OutwardService {

    private OutwardDao outwardDao = new OutwardDao();
    private BedDao bedDao = new BedDao();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 查询所有外出记录。
     * 实现：调用outwardDao.listAll查询全部，多条时按外出日期排序。
     */
    public List<Outward> listAll() {
        try {
            List<Outward> outwards = outwardDao.listAll();
            if (outwards != null && outwards.size() > 1) {
                Collections.sort(outwards, SortUtil.OUTWARD_SORT);
            }
            return outwards;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询外出记录。
     * 实现：调用outwardDao.getById按ID查询并返回。
     */
    public Outward getById(int id) {
        try {
            return outwardDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增外出记录。
     * 实现：调用outwardDao.add写入外出记录。
     */
    public void addOutward(Outward outward) {
        try {
            outwardDao.add(outward);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新外出记录。
     * 实现：调用outwardDao.update更新外出记录。
     */
    public void updateOutward(Outward outward) {
        try {
            outwardDao.update(outward);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除外出记录。
     * 实现：先调outwardDao.getById获取，设isDeleted=1后调outwardDao.update更新。
     */
    public void deleteOutward(int id) {
        try {
            Outward outward = outwardDao.getById(id);
            if (outward != null) {
                outward.setIsDeleted(1);
                outwardDao.update(outward);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核外出记录。
     * 实现：先调outwardDao.getById获取，设审核状态和审核人后调outwardDao.update更新。
     */
    public void auditOutward(int id, int auditStatus, String auditPerson) {
        try {
            Outward outward = outwardDao.getById(id);
            if (outward == null) {
                System.out.println("外出记录不存在");
                return;
            }

            String today = LocalDate.now().format(DATE_FORMATTER);
            outward.setAuditStatus(auditStatus);
            outward.setAuditPerson(auditPerson);
            outward.setAuditTime(today);
            outwardDao.update(outward);

            // 审核通过
            if (auditStatus == AuditStatus.APPROVED.getCode()) {
                // 审核通过：根据外出记录的客户ID找到对应Customer和床位，设床位状态为外出(3)
                CustomerDao customerDao = new CustomerDao();
                com.neuedu.tms.pojo.Customer c = customerDao.getById(outward.getCustomerId());
                if (c != null && c.getBedId() > 0) {
                    Bed bed = bedDao.getById(c.getBedId());
                    if (bed != null) {
                        bed.setBedStatus(BedStatus.OUT.getCode()); // 外出
                        bedDao.update(bed);
                    }
                }
                System.out.println("外出审核通过，客户ID: " + outward.getCustomerId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登记外出返回，设置实际返回时间并恢复床位状态为有人。
     * 实现：先调outwardDao.getById获取，设实际返回时间后调update更新。
     */
    public void registerReturn(int id, String actualReturnTime) {
        try {
            Outward outward = outwardDao.getById(id);
            if (outward == null) {
                System.out.println("外出记录不存在");
                return;
            }

            outward.setActualReturnTime(actualReturnTime);
            outwardDao.update(outward);

            // 回院登记：恢复床位状态为有人(2)
            CustomerDao customerDao = new CustomerDao();
            com.neuedu.tms.pojo.Customer c = customerDao.getById(outward.getCustomerId());
            if (c != null && c.getBedId() > 0) {
                Bed bed = bedDao.getById(c.getBedId());
                if (bed != null) {
                    bed.setBedStatus(BedStatus.OCCUPIED.getCode()); // 恢复为有人
                    bedDao.update(bed);
                }
            }

            System.out.println("外出返回登记完成，客户ID: " + outward.getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
