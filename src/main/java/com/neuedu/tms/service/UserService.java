package com.neuedu.tms.service;

import com.neuedu.tms.dao.UserDao;
import com.neuedu.tms.dao.CustomerDao;
import com.neuedu.tms.enums.RoleEnum;
import com.neuedu.tms.pojo.User;
import com.neuedu.tms.pojo.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.neuedu.tms.utils.SortUtil;

public class UserService {

    private UserDao userDao = new UserDao();

    /**
     * 用户登录验证。
     * 实现：调用userDao.login匹配用户名和密码，返回匹配的User对象或null。
     */
    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    /**
     * 查询所有未删除的用户。
     * 实现：调用userDao.listAll查询全部，多条时按角色和姓名排序。
     */
    public List<User> listAll() {
        try {
            List<User> users = userDao.listAll();
            if (users != null && users.size() > 1) {
                Collections.sort(users, SortUtil.BY_ROLE_AND_NAME);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ID查询用户。
     * 实现：调用userDao.getById按ID查询并返回。
     */
    public User getById(int id) {
        try {
            return userDao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增用户，校验用户名唯一性后写入。
     * 实现：先调userDao.existsByUsername检查重复，无重复则设创建时间和默认值后调userDao.add写入。
     */
    public boolean addUser(User user) {
        try {
            if (userDao.existsByUsername(user.getUsername())) {
                System.out.println("用户名 '" + user.getUsername() + "' 已存在，添加失败！");
                return false;
            }
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            user.setCreateTime(today);
            user.setIsDeleted(0);
            userDao.add(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户信息。
     * 实现：调用userDao.update更新用户记录。
     */
    public void updateUser(User user) {
        try {
            userDao.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑删除用户。
     * 实现：调用userDao.delete删除用户记录。
     */
    public void deleteUser(int id) {
        try {
            userDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据角色ID查询用户列表。
     * 实现：调用userDao.listByRole按角色查询，多条时按昵称排序。
     */
    public List<User> listByRole(int roleId) {
        try {
            List<User> users = userDao.listByRole(roleId);
            if (users != null && users.size() > 1) {
                Collections.sort(users, SortUtil.BY_NICKNAME);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据姓名模糊搜索用户。
     * 实现：调用userDao.searchByName按关键字模糊匹配，多条时按昵称排序。
     */
    public List<User> searchByName(String keyword) {
        try {
            List<User> users = userDao.searchByName(keyword);
            if (users != null && users.size() > 1) {
                Collections.sort(users, SortUtil.BY_NICKNAME);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有健康管家（roleId=2）。
     * 实现：调用userDao.listByRole(2)查询角色为2的用户，多条时按昵称排序。
     */
    public List<User> listStewards() {
        try {
            List<User> users = userDao.listByRole(RoleEnum.NURSE.getRoleId());
            if (users != null && users.size() > 1) {
                Collections.sort(users, SortUtil.BY_NICKNAME);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为客户分配健康管家，校验管家角色后更新客户的userId。
     * 实现：先调userDao.getById校验管家存在且角色为2，再调customerDao.update设置customer.userId。
     */
    public void assignSteward(int customerId, int stewardId) {
        try {
            CustomerDao customerDao = new CustomerDao();
            User steward = userDao.getById(stewardId);
            if (steward == null || steward.getRoleId() != RoleEnum.NURSE.getRoleId()) {
                System.out.println("指定的管家不存在或角色不是健康管家");
                return;
            }
            Customer customer = customerDao.getById(customerId);
            if (customer != null) {
                customer.setUserId(stewardId);
                customerDao.update(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除客户的健康管家，将客户userId置空。
     * 实现：调customerDao.getById获取客户后设userId为null，再调customerDao.update更新。
     */
    public void removeSteward(int customerId) {
        try {
            CustomerDao customerDao = new CustomerDao();
            Customer customer = customerDao.getById(customerId);
            if (customer != null) {
                customer.setUserId(null);
                customerDao.update(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
