package com.neuedu.tms.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * 控制台输入工具类 - 提供健壮的输入验证
 * 所有方法均使用 nextLine() 读取后再解析，避免 Scanner 缓冲区问题
 */
public class InputUtil {

    /// 全局共享的 Scanner 实例，避免重复创建
    private static final Scanner SCANNER = new Scanner(System.in);
    /// 统一日期格式：yyyy-MM-dd
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 读取一个非空字符串
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("输入不能为空，请重新输入。");
        }
    }

    /**
     * 读取字符串，允许使用默认值（直接回车返回默认值）
     */
    public static String readString(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = SCANNER.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        return input;
    }

    /**
     * 读取一个整数（循环直到输入合法）
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请输入一个整数。");
            }
        }
    }

    /**
     * 读取指定范围内的整数
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("输入超出范围，请输入 " + min + " 到 " + max + " 之间的整数。");
        }
    }

    /**
     * 读取一个可选的整数（空输入返回 null）
     */
    public static Integer readOptionalInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请输入整数或直接回车跳过。");
            }
        }
    }

    /**
     * 读取一个浮点数
     */
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请输入一个数字。");
            }
        }
    }

    /**
     * 读取日期（格式 yyyy-MM-dd）
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + "(yyyy-MM-dd): ");
            String input = SCANNER.nextLine().trim();
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请输入 yyyy-MM-dd 格式的日期。");
            }
        }
    }

    /**
     * 读取可选的日期（空输入返回 null）
     */
    public static LocalDate readOptionalDate(String prompt) {
        while (true) {
            System.out.print(prompt + "(yyyy-MM-dd, 直接回车跳过): ");
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请输入 yyyy-MM-dd 格式的日期。");
            }
        }
    }

    /**
     * 确认操作（y/n）
     */
    public static boolean readConfirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = SCANNER.nextLine().trim().toLowerCase();
            if ("y".equals(input) || "yes".equals(input)) {
                return true;
            }
            if ("n".equals(input) || "no".equals(input)) {
                return false;
            }
            System.out.println("请输入 y 或 n。");
        }
    }

    /**
     * 显示编号菜单并读取用户选择（1-based 索引）
     *
     * @param prompt  提示信息
     * @param options 菜单选项数组
     * @return 用户选择的序号（从 1 开始）
     */
    public static int readChoice(String prompt, String[] options) {
        System.out.println(prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ". " + options[i]);
        }
        return readInt("请选择 (1-" + options.length + "): ", 1, options.length);
    }

    /**
     * 等待用户按回车键继续（不创建新 Scanner，复用全局 Scanner）
     */
    public static void pressEnter(String prompt) {
        System.out.print(prompt);
        SCANNER.nextLine();
    }
}
