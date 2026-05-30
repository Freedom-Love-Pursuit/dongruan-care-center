package com.neuedu.tms.service;

import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.dao.BedDao;
import com.neuedu.tms.enums.BedStatus;
import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.pojo.Bed;
import com.neuedu.tms.pojo.BedDetail;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class CustomerService {

    private CustomerDao customerDao = new CustomerDao();
    private BedDao bedDao = new BedDao();

    /**
     * 查询所有未删除的客户。
     * 实现：调用customerDao.listAll查询全部，多条时按入住日期降序排序。
     */
    public List<Customer> listAll() {
        try {
            List<Customer> customers = customerDao.listAll();
            if (customers != null && customers.size() > 1) {
                Collections.sort(customers, SortUtil.BY_CHECKIN_DATE_DESC);
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询客户。
     * 实现：调用customerDao.getById按ID查询并返回。
     */
    public Customer getById(int id) {
        try {
            return customerDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增客户，校验身份证唯一性后自动计算年龄并设置默认楼号。
     * 实现：先调customerDao.existsByIdcard检查重复，无重复则根据生日计算年龄、设置楼号606，最后调customerDao.add写入。
     */
    public Customer addCustomer(Customer customer) {
        try {
            if (customerDao.existsByIdcard(customer.getIdcard())) {
                System.out.println("身份证号 '" + customer.getIdcard() + "' 已存在，入住登记失败！");
                return null;
            }
            // 自动根据生日计算年龄
            if (customer.getBirthday() != null && !customer.getBirthday().isEmpty()) {
                LocalDate birthday = LocalDate.parse(customer.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int age = Period.between(birthday, LocalDate.now()).getYears();
                customer.setCustomerAge(age);
            }
            customer.setIsDeleted(0);
            if (customer.getBuildingNo() == null || customer.getBuildingNo().isEmpty()) {
                customer.setBuildingNo("606");
            }
            customerDao.add(customer);
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新客户信息，重新计算年龄。
     * 实现：先根据生日重新计算年龄，再调customerDao.update更新客户记录。
     */
    public Customer updateCustomer(Customer customer) {
        try {
            // 重新计算年龄
            if (customer.getBirthday() != null && !customer.getBirthday().isEmpty()) {
                LocalDate birthday = LocalDate.parse(customer.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int age = Period.between(birthday, LocalDate.now()).getYears();
                customer.setCustomerAge(age);
            }
            customerDao.update(customer);
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 逻辑删除客户，释放床位，并结束当前床位详情记录。
     * 实现：获取客户→设isDeleted=1→若占用床位则释放为空闲(1)→结束当前BedDetail。
     */
    public void deleteCustomer(int id) {
        try {
            Customer customer = customerDao.getById(id);
            if (customer != null) {
                customer.setIsDeleted(1);
                customerDao.update(customer);

                if (customer.getBedId() > 0) {
                    Bed bed = bedDao.getById(customer.getBedId());
                    if (bed != null) {
                        bed.setBedStatus(BedStatus.FREE.getCode()); // 空闲
                        bedDao.update(bed);
                    }
                    // 结束当前床位详情记录
                    BedDetailService bds = new BedDetailService();
                    BedDetail oldDetail = bds.getCurrentByCustomerId(customer.getId());
                    if (oldDetail != null) {
                        bds.endBedDetail(oldDetail.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据姓名模糊搜索客户。
     * 实现：调用customerDao.searchByName按关键字模糊匹配，多条时按入住日期降序排序。
     */
    public List<Customer> searchByName(String keyword) {
        try {
            List<Customer> customers = customerDao.searchByName(keyword);
            if (customers != null && customers.size() > 1) {
                Collections.sort(customers, SortUtil.BY_CHECKIN_DATE_DESC);
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据类型查询客户（自理/护理）。
     * 实现：调用customerDao.listByType按类型查询，多条时按入住日期降序排序。
     */
    public List<Customer> listByType(String type) {
        try {
            List<Customer> customers = customerDao.listByType(type);
            if (customers != null && customers.size() > 1) {
                Collections.sort(customers, SortUtil.BY_CHECKIN_DATE_DESC);
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询没有分配健康管家的客户。
     * 实现：调用customerDao.listWithoutSteward查询，多条时按姓名排序。
     */
    public List<Customer> listWithoutSteward() {
        try {
            List<Customer> customers = customerDao.listWithoutSteward();
            if (customers != null && customers.size() > 1) {
                Collections.sort(customers, SortUtil.BY_NAME);
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据管家ID查询其负责的客户列表。
     * 实现：调用customerDao.listBySteward按管家ID查询，多条时按姓名排序。
     */
    public List<Customer> listBySteward(int userId) {
        try {
            List<Customer> customers = customerDao.listBySteward(userId);
            if (customers != null && customers.size() > 1) {
                Collections.sort(customers, SortUtil.BY_NAME);
            }
            return customers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
