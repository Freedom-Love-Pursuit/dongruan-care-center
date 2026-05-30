package com.neuedu.tms.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ID 自增生成器
 * 通过读取 JSON 数据文件中的最大 ID 来计算下一个 ID
 */
public class IdGenerator {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String DATA_DIR = "data";

    private static final TypeReference<List<Map<String, Object>>> MAP_LIST_TYPE =
            new TypeReference<List<Map<String, Object>>>() {};

    /**
     * 获取下一个可用的自增 ID
     * 读取指定 JSON 文件中所有记录的 "id" 字段，返回最大值 + 1
     *
     * @param fileName JSON 文件名（如 "users.json"）
     * @return 下一个可用 ID，若文件为空则返回 1
     */
    public static int nextId(String fileName) {
        List<Map<String, Object>> list = readJsonFileAsMapList(fileName);
        if (list == null || list.isEmpty()) {
            return 1;
        }

        int maxId = 0;
        for (Map<String, Object> record : list) {
            Object idObj = record.get("id");
            if (idObj != null) {
                int id;
                if (idObj instanceof Integer) {
                    id = (Integer) idObj;
                } else if (idObj instanceof Number) {
                    id = ((Number) idObj).intValue();
                } else {
                    try {
                        id = Integer.parseInt(idObj.toString());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    /// 将 JSON 文件读取为 List<Map<String, Object>> 格式，文件不存在时返回空列表
    private static List<Map<String, Object>> readJsonFileAsMapList(String fileName) {
        try {
            File file = new File(DATA_DIR, fileName);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return MAPPER.readValue(file, MAP_LIST_TYPE);
        } catch (IOException e) {
            System.err.println("读取文件失败: " + fileName);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
