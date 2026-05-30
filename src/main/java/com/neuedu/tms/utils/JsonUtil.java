package com.neuedu.tms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 工具类 - 统一处理所有 JSON 文件读写操作
 * 使用 Jackson 进行序列化/反序列化，支持 Java 8 时间类型
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private static final String DATA_DIR = "data";

    /**
     * 获取数据文件的完整路径，确保 data 目录存在
     */
    private static File getDataFile(String fileName) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, fileName);
    }

    /**
     * 从 JSON 文件中读取对象列表
     *
     * @param fileName JSON 文件名（如 "users.json"）
     * @param clazz    目标类型
     * @param <T>      泛型类型
     * @return 对象列表，文件不存在或读取失败时返回空列表
     */
    public static <T> List<T> readList(String fileName, Class<T> clazz) {
        try {
            File file = getDataFile(fileName);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return MAPPER.readValue(file,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            System.err.println("读取文件失败: " + fileName);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 将对象列表写入 JSON 文件（带格式化缩进）
     *
     * @param fileName JSON 文件名（如 "users.json"）
     * @param list     要写入的对象列表
     * @param <T>      泛型类型
     */
    public static <T> void writeList(String fileName, List<T> list) {
        try {
            File file = getDataFile(fileName);
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (IOException e) {
            System.err.println("写入文件失败: " + fileName);
            e.printStackTrace();
        }
    }
}
