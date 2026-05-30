package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.NurseLevel;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NurseLevelDao {

    private static final String DATA_FILE = "nurselevel.json";

    /**
     * 查询所有护理等级列表。
     * 实现：读取nurselevel.json全量数据后返回。
     */
    public List<NurseLevel> listAll() {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 查询所有启用状态的护理等级列表。
     * 实现：过滤nurselevel.json中levelStatus==1的记录后返回。
     */
    public List<NurseLevel> listActive() {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list.stream()
                    .filter(l -> l.getLevelStatus() == 1)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 校验护理等级名称是否已存在（排除已删除记录）。
     * 实现：遍历nurselevel.json，匹配levelName且isDeleted==0后返回布尔。
     */
    public boolean existsByName(String levelName) {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) return false;
            for (NurseLevel l : list) {
                if (l.getIsDeleted() == 0 && levelName.equals(l.getLevelName())) {
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
     * 按ID查询护理等级记录。
     * 实现：遍历nurselevel.json列表，匹配id后返回。
     */
    public NurseLevel getById(int id) {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                return null;
            }
            for (NurseLevel l : list) {
                if (l.getId() == id) {
                    return l;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增护理等级，自动生成ID，重名校验通过后存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，校验名称不重复后追加列表写回nurselevel.json。
     */
    public void add(NurseLevel level) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            level.setId(nextId);
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            // Check for duplicate level name
            for (NurseLevel l : list) {
                if (l.getIsDeleted() == 0 && level.getLevelName().equals(l.getLevelName())) {
                    System.out.println("护理等级名称 '" + level.getLevelName() + "' 已存在，无法重复添加！");
                    return;
                }
            }
            list.add(level);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新护理等级记录。
     * 实现：遍历列表匹配id，替换后写回nurselevel.json。
     */
    public void update(NurseLevel level) {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == level.getId()) {
                    list.set(i, level);
                    JsonUtil.writeList(DATA_FILE, list);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按状态搜索护理等级列表。
     * 实现：读取nurselevel.json，若status不为空则过滤levelStatus匹配的记录。
     */
    public List<NurseLevel> searchByStatus(Integer status) {
        try {
            List<NurseLevel> list = JsonUtil.readList(DATA_FILE, NurseLevel.class);
            if (list == null) {
                return new ArrayList<>();
            }
            if (status == null) {
                return list;
            }
            return list.stream()
                    .filter(l -> l.getLevelStatus() == status)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
