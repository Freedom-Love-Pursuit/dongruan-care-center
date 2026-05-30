package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.Preference;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class PreferenceDao {

    private static final String DATA_FILE = "preference.json";

    /**
     * 按客户ID查询未删除的偏好记录。
     * 实现：遍历preference.json，匹配customerId且isDeleted==0后返回。
     */
    public Preference getByCustomerId(int customerId) {
        try {
            List<Preference> preferences = JsonUtil.readList(DATA_FILE, Preference.class);
            if (preferences == null) {
                return null;
            }
            for (Preference p : preferences) {
                if (p.getCustomerId() == customerId && p.getIsDeleted() == 0) {
                    return p;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增偏好记录，自动生成ID并存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，追加到列表后写回preference.json。
     */
    public void add(Preference p) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            p.setId(nextId);
            List<Preference> preferences = JsonUtil.readList(DATA_FILE, Preference.class);
            if (preferences == null) {
                preferences = new ArrayList<>();
            }
            preferences.add(p);
            JsonUtil.writeList(DATA_FILE, preferences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新偏好记录。
     * 实现：遍历列表匹配id，替换后写回preference.json。
     */
    public void update(Preference p) {
        try {
            List<Preference> preferences = JsonUtil.readList(DATA_FILE, Preference.class);
            if (preferences == null) {
                return;
            }
            for (int i = 0; i < preferences.size(); i++) {
                if (preferences.get(i).getId() == p.getId()) {
                    preferences.set(i, p);
                    JsonUtil.writeList(DATA_FILE, preferences);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查客户是否已有偏好记录（排除已删除记录）。
     * 实现：遍历preference.json，匹配customerId且isDeleted==0后返回布尔。
     */
    public boolean existsByCustomerId(int customerId) {
        try {
            List<Preference> preferences = JsonUtil.readList(DATA_FILE, Preference.class);
            if (preferences == null) {
                return false;
            }
            for (Preference p : preferences) {
                if (p.getIsDeleted() == 0 && p.getCustomerId() == customerId) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
