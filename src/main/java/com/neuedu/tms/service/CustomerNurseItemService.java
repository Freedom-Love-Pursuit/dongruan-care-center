package com.neuedu.tms.service;

import com.neuedu.tms.dao.CustomerNurseItemDao;
import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.dao.NurseLevelItemDao;
import com.neuedu.tms.pojo.CustomerNurseItem;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.NurseContent;
import com.neuedu.tms.pojo.NurseLevelItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerNurseItemService {

    private CustomerNurseItemDao customerNurseItemDao = new CustomerNurseItemDao();
    private CustomerDao customerDao = new CustomerDao();
    private NurseLevelItemDao nurseLevelItemDao = new NurseLevelItemDao();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 为客户设置护理等级，更新客户等级并批量添加该等级下所有护理项目。
     * 实现：先调customerDao.update更新客户levelId，再调customerNurseItemDao.deleteByCustomerAndLevel移除旧项目，最后遍历nurseLevelItemDao.listByLevelId结果逐条添加。
     */
    public void setCustomerLevel(int customerId, int levelId) {
        try {
            // 更新客户护理等级
            Customer customer = customerDao.getById(customerId);
            if (customer == null) {
                System.out.println("客户不存在");
                return;
            }
            customer.setLevelId(levelId);
            customerDao.update(customer);

            // 移除客户现有的所有护理等级相关项目
            customerNurseItemDao.deleteByCustomerAndLevel(customerId);

            // 获取该等级下的所有护理项目
            List<NurseLevelItem> levelItems = nurseLevelItemDao.listByLevelId(levelId);

            // 批量添加
            String today = LocalDate.now().format(DATE_FORMATTER);
            String maturityTime = LocalDate.now().plusMonths(3).format(DATE_FORMATTER);

            for (NurseLevelItem li : levelItems) {
                CustomerNurseItem item = new CustomerNurseItem();
                item.setCustomerId(customerId);
                item.setItemId(li.getItemId());
                item.setBuyTime(today);
                item.setMaturityTime(maturityTime);
                item.setNurseNumber(1);
                item.setIsDeleted(0);
                customerNurseItemDao.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除客户的护理等级，清空等级字段并删除所有等级相关项目。
     * 实现：先调customerDao.update将客户levelId置空，再调customerNurseItemDao.deleteByCustomerAndLevel删除等级关联项目。
     */
    public void removeCustomerLevel(int customerId) {
        try {
            // 清空客户的护理等级
            Customer customer = customerDao.getById(customerId);
            if (customer != null) {
                customer.setLevelId(null);
                customerDao.update(customer);
            }

            // 删除客户所有等级相关的护理项目
            customerNurseItemDao.deleteByCustomerAndLevel(customerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询客户的所有护理项目。
     * 实现：调用customerNurseItemDao.listByCustomerId按客户ID查询并返回。
     */
    public List<CustomerNurseItem> listByCustomerId(int customerId) {
        try {
            return customerNurseItemDao.listByCustomerId(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 客户单独购买护理项目，设置默认有效期为3个月。
     * 实现：遍历itemIds列表，逐一构建CustomerNurseItem对象并调customerNurseItemDao.add写入。
     */
    public void buyItems(int customerId, List<Integer> itemIds) {
        try {
            String today = LocalDate.now().format(DATE_FORMATTER);
            String maturityTime = LocalDate.now().plusMonths(3).format(DATE_FORMATTER);

            for (Integer itemId : itemIds) {
                CustomerNurseItem item = new CustomerNurseItem();
                item.setCustomerId(customerId);
                item.setItemId(itemId);
                item.setBuyTime(today);
                item.setMaturityTime(maturityTime);
                item.setNurseNumber(1);
                item.setIsDeleted(0);
                customerNurseItemDao.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 续费护理项目，增加护理次数并更新到期时间。
     * 实现：先调customerNurseItemDao.getByCustomerAndItem获取记录，累加次数后调update更新。
     */
    public void renewItem(int customerId, int itemId, int addCount, String newMaturityDate) {
        try {
            CustomerNurseItem item = customerNurseItemDao.getByCustomerAndItem(customerId, itemId);
            if (item != null) {
                int currentNumber = item.getNurseNumber();
                item.setNurseNumber(currentNumber + addCount);
                if (newMaturityDate != null && !newMaturityDate.isEmpty()) {
                    item.setMaturityTime(newMaturityDate);
                }
                customerNurseItemDao.update(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除客户的某个护理项目（逻辑删除）。
     * 实现：先调customerNurseItemDao.getByCustomerAndItem获取，设isDeleted=1后调update更新。
     */
    public void removeItem(int customerId, int itemId) {
        try {
            CustomerNurseItem item = customerNurseItemDao.getByCustomerAndItem(customerId, itemId);
            if (item != null) {
                item.setIsDeleted(1);
                customerNurseItemDao.update(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取客户可购买的可用护理项目（启用且未拥有的）。
     * 实现：调nurseContentDao.listActive获取所有启用项目，再调customerNurseItemDao.listByCustomerId获取已拥有项目，过滤后返回差异列表。
     */
    public List<NurseContent> getAvailableItems(int customerId) {
        try {
            // 需要从NurseContentDao获取所有启用的项目
            com.neuedu.tms.dao.NurseContentDao nurseContentDao = new com.neuedu.tms.dao.NurseContentDao();
            List<NurseContent> allActive = nurseContentDao.listActive();

            // 获取客户已有的项目
            List<CustomerNurseItem> customerItems = customerNurseItemDao.listByCustomerId(customerId);

            List<Integer> ownedIds = new ArrayList<>();
            for (CustomerNurseItem ci : customerItems) {
                ownedIds.add(ci.getItemId());
            }

            List<NurseContent> available = new ArrayList<>();
            for (NurseContent nc : allActive) {
                if (!ownedIds.contains(nc.getId())) {
                    available.add(nc);
                }
            }
            return available;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取客户护理项目的服务状态列表，包含到期、欠费、未到期等状态。
     * 实现：调customerNurseItemDao.listByCustomerId获取项目列表，逐项比对到期时间和护理次数计算状态。
     */
    public List<Map<String, Object>> getServiceStatus(int customerId) {
        try {
            List<Map<String, Object>> statusList = new ArrayList<>();
            List<CustomerNurseItem> items = customerNurseItemDao.listByCustomerId(customerId);
            LocalDate today = LocalDate.now();

            com.neuedu.tms.dao.NurseContentDao nurseContentDao = new com.neuedu.tms.dao.NurseContentDao();

            for (CustomerNurseItem item : items) {
                Map<String, Object> statusMap = new HashMap<>();
                NurseContent content = nurseContentDao.getById(item.getItemId());

                statusMap.put("id", item.getId());
                statusMap.put("customerId", item.getCustomerId());
                statusMap.put("itemId", item.getItemId());
                statusMap.put("itemName", content != null ? content.getNursingName() : "未知项目");
                statusMap.put("nurseNumber", item.getNurseNumber());
                statusMap.put("buyTime", item.getBuyTime());
                statusMap.put("maturityTime", item.getMaturityTime());

                // 计算状态
                String status;
                if (item.getMaturityTime() != null && !item.getMaturityTime().isEmpty()) {
                    LocalDate maturity = LocalDate.parse(item.getMaturityTime(), DATE_FORMATTER);
                    if (today.isAfter(maturity)) {
                        status = "到期";
                    } else if (item.getNurseNumber() <= 0) {
                        status = "欠费";
                    } else {
                        status = "未到期";
                    }
                } else {
                    status = "数量正常";
                }
                statusMap.put("status", status);

                statusList.add(statusMap);
            }
            return statusList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
