package com.neuedu.tms.dao;

import com.neuedu.tms.pojo.NurseContent;
import com.neuedu.tms.utils.JsonUtil;
import com.neuedu.tms.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NurseContentDao {

    private static final String DATA_FILE = "nursecontent.json";

    /**
     * 查询所有护理内容记录列表。
     * 实现：读取nursecontent.json全量数据后返回。
     */
    public List<NurseContent> listAll() {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
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
     * 查询所有启用且未删除的护理内容列表。
     * 实现：过滤nursecontent.json中status==1且isDeleted==0的记录后返回。
     */
    public List<NurseContent> listActive() {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list.stream()
                    .filter(n -> n.getStatus() == 1 && n.getIsDeleted() == 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 校验护理内容名称是否已存在（排除已删除记录）。
     * 实现：遍历nursecontent.json，匹配nursingName且isDeleted==0后返回布尔。
     */
    public boolean existsByName(String nursingName) {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) return false;
            for (NurseContent n : list) {
                if (n.getIsDeleted() == 0 && nursingName.equals(n.getNursingName())) {
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
     * 按ID查询护理内容记录。
     * 实现：遍历nursecontent.json列表，匹配id后返回。
     */
    public NurseContent getById(int id) {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                return null;
            }
            for (NurseContent n : list) {
                if (n.getId() == id) {
                    return n;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增护理内容，自动生成ID，重名校验通过后存入JSON文件。
     * 实现：调用IdGenerator获取自增ID，校验名称不重复后追加列表写回nursecontent.json。
     */
    public void add(NurseContent nc) {
        try {
            int nextId = IdGenerator.nextId(DATA_FILE);
            nc.setId(nextId);
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            // Check for duplicate nursing name
            for (NurseContent n : list) {
                if (n.getIsDeleted() == 0 && nc.getNursingName().equals(n.getNursingName())) {
                    System.out.println("护理内容名称 '" + nc.getNursingName() + "' 已存在，无法重复添加！");
                    return;
                }
            }
            list.add(nc);
            JsonUtil.writeList(DATA_FILE, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID更新护理内容记录。
     * 实现：遍历列表匹配id，替换后写回nursecontent.json。
     */
    public void update(NurseContent nc) {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == nc.getId()) {
                    list.set(i, nc);
                    JsonUtil.writeList(DATA_FILE, list);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按ID软删除护理内容记录。
     * 实现：遍历列表匹配id，设置isDeleted=1后写回nursecontent.json。
     */
    public void delete(int id) {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                return;
            }
            for (NurseContent n : list) {
                if (n.getId() == id) {
                    n.setIsDeleted(1);
                    JsonUtil.writeList(DATA_FILE, list);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按关键词和状态搜索护理内容。
     * 实现：过滤isDeleted==0，支持按nursingName/message模糊匹配和status精确匹配。
     */
    public List<NurseContent> search(String keyword, Integer status) {
        try {
            List<NurseContent> list = JsonUtil.readList(DATA_FILE, NurseContent.class);
            if (list == null) {
                return new ArrayList<>();
            }
            return list.stream()
                    .filter(n -> n.getIsDeleted() == 0)
                    .filter(n -> {
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            String lowerKeyword = keyword.toLowerCase();
                            boolean nameMatch = n.getNursingName() != null
                                    && n.getNursingName().toLowerCase().contains(lowerKeyword);
                            boolean msgMatch = n.getMessage() != null
                                    && n.getMessage().toLowerCase().contains(lowerKeyword);
                            if (!nameMatch && !msgMatch) {
                                return false;
                            }
                        }
                        if (status != null) {
                            if (n.getStatus() != status) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
