package com.neuedu.tms.enums;

/**
 * 性别枚举 —— 消除性别魔法数字
 */
public enum Gender {

    FEMALE(0, "女"),
    MALE(1,   "男");

    private int code;
    private String text;

    Gender(int code, String text) { this.code = code; this.text = text; }

    public int getCode() { return code; }
    public String getText() { return text; }

    public static String textOf(int code) {
        for (Gender g : values()) {
            if (g.code == code) return g.text;
        }
        return "未知";
    }
}
