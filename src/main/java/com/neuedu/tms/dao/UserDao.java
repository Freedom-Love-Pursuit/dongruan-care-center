package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.User;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    private static final String DATA_FILE = "user.json";

    /**
     * 查询所有未删除的用户列表。
     * 实现：读取user.json，过滤isDeleted==0后返回。
     */
    public List<User> listAll() {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return new ArrayList<>();
            }
            return users.stream()
                    .filter(u -> u.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按ID查询未删除的用户。
     * 实现：遍历user.json，匹配id且isDeleted==0后返回。
     */
    public User getById(int id) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return null;
            }
            for (User u : users) {
                if (u.getId() == id && u.getIsDeleted() == 0) {
                    return u;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户登录验证。
     * 实现：遍历user.json，匹配未删除用户的username和password后返回。
     */
    public User login(String username, String password) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return null;
            }
            for (User u : users) {
                if (u.getIsDeleted() == 0
                        && username.equals(u.getUsername())
                        && password.equals(u.getPassword())) {
                    return u;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增用户，自动生成ID，校验用户名唯一性后存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，校验用户名不重复后追加列表写回user.json。
     */
    public void add(User user) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            user.setId(nextId);
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                users = new ArrayList<>();
            }
            // Check for duplicate username
            for (User u : users) {
                if (u.getIsDeleted() == 0 && user.getUsername().equals(u.getUsername())) {
                    System.out.println("用户名 '" + user.getUsername() + "' 已存在，无法重复添加！");
                    return;
                }
            }
            users.add(user);
            JsonUtil.writeList(DATA_FILE, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新用户信息。
     * 实现：遍历列表匹配id，替换后写回user.json。
     */
    public void update(User user) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return;
            }
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    JsonUtil.writeList(DATA_FILE, users);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除用户。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回user.json。
     */
    public void delete(int id) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return;
            }
            for (User u : users) {
                if (u.getId() == id) {
                    u.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, users);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验用户名是否已存在（排除已删除记录）。
     * 实现：遍历user.json，匹配username且isDeleted==0后返回布尔。
     */
    public boolean existsByUsername(String username) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) return false;
            for (User u : users) {
                if (u.getIsDeleted() == 0 && username.equals(u.getUsername())) {
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
     * 按角色ID查询未删除的用户列表。
     * 实现：过滤user.json中isDeleted==0且roleId匹配的记录。
     */
    public List<User> listByRole(int roleId) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return new ArrayList<>();
            }
            return users.stream()
                    .filter(u -> u.getIsDeleted() == 0 && u.getRoleId() == roleId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 按昵称或用户名模糊搜索。
     * 实现：读取user.json，过滤isDeleted==0，匹配nickname或username包含关键词后返回。
     */
    public List<User> searchByName(String keyword) {
        try {
            List<User> users = JsonUtil.readList(DATA_FILE, User.class);
            if (users == null) {
                return new ArrayList<>();
            }
            String lowerKeyword = keyword.toLowerCase();
            return users.stream()
                    .filter(u -> u.getIsDeleted() == 0
                            && (u.getNickname() != null && u.getNickname().toLowerCase().contains(lowerKeyword)
                                || u.getUsername() != null && u.getUsername().toLowerCase().contains(lowerKeyword)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
