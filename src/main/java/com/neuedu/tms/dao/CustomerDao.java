package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Customer;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDao {

    private static final String DATA_FILE = "customer.json";

    /**
     * 查询所有未删除的客户列表。
     * 实现：读取customer.json，过滤isDeleted==0后返回。
     */
    public List<Customer> listAll() {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return new ArrayList<>();
            }
            return customers.stream()
                    .filter(c -> c.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询未删除的客户。
     * 实现：遍历customer.json，匹配id且isDeleted==0后返回。
     */
    public Customer getById(int id) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return null;
            }
            for (Customer c : customers) {
                if (c.getId() == id && c.getIsDeleted() == 0) {
                    return c;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增客户，自动生成ID，校验身份证号唯一性后存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，校验身份证不重复后追加列表写回customer.json。
     */
    public void add(Customer customer) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            customer.setId(nextId);
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                customers = new ArrayList<>();
            }
            // Check for duplicate idcard
            for (Customer c : customers) {
                if (c.getIsDeleted() == 0 && customer.getIdcard().equals(c.getIdcard())) {
                    System.out.println("身份证号 '" + customer.getIdcard() + "' 已存在，无法重复添加！");
                    return;
                }
            }
            customers.add(customer);
            JsonUtil.writeList(DATA_FILE, customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新客户信息。
     * 实现：遍历列表匹配id，替换后写回customer.json。
     */
    public void update(Customer customer) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return;
            }
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getId() == customer.getId()) {
                    customers.set(i, customer);
                    JsonUtil.writeList(DATA_FILE, customers);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验身份证号是否已存在（排除已删除记录）。
     * 实现：遍历customer.json，匹配idcard且isDeleted==0后返回布尔。
     */
    public boolean existsByIdcard(String idcard) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) return false;
            for (Customer c : customers) {
                if (c.getIsDeleted() == 0 && idcard.equals(c.getIdcard())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 按ID软删除客户。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回customer.json。
     */
    public void delete(int id) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return;
            }
            for (Customer c : customers) {
                if (c.getId() == id) {
                    c.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, customers);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按客户姓名模糊搜索。
     * 实现：读取customer.json，过滤isDeleted==0且customerName包含关键词后返回。
     */
    public List<Customer> searchByName(String keyword) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return new ArrayList<>();
            }
            String lowerKeyword = keyword.toLowerCase();
            return customers.stream()
                    .filter(c -> c.getIsDeleted() == 0
                            && c.getCustomerName() != null
                            && c.getCustomerName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按类型查询客户：selfcare查levelId为空的，nursing查levelId不为空的。
     * 实现：读取customer.json，根据type参数过滤后返回。
     */
    public List<Customer> listByType(String type) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return new ArrayList<>();
            }
            if ("selfcare".equals(type)) {
                return customers.stream()
                        .filter(c -> c.getIsDeleted() == 0 && c.getLevelId() == null)
                        .collect(Collectors.toList());
            } else if ("nursing".equals(type)) {
                return customers.stream()
                        .filter(c -> c.getIsDeleted() == 0 && c.getLevelId() != null)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 查询未分配管家的客户列表。
     * 实现：过滤customer.json中isDeleted==0且userId为空的记录。
     */
    public List<Customer> listWithoutSteward() {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return new ArrayList<>();
            }
            return customers.stream()
                    .filter(c -> c.getIsDeleted() == 0 && c.getUserId() == null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按管家ID查询其负责的客户列表。
     * 实现：过滤customer.json中userId匹配且isDeleted==0的记录。
     */
    public List<Customer> listBySteward(int userId) {
        try {
            List<Customer> customers = JsonUtil.readList(DATA_FILE, Customer.class);
            if (customers == null) {
                return new ArrayList<>();
            }
            return customers.stream()
                    .filter(c -> c.getIsDeleted() == 0
                            && c.getUserId() != null
                            && c.getUserId() == userId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
