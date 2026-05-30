package com.neuedu.tms.enums;

/**
 * 退住类型枚举 —— 消除退住类型魔法数字
 */
public enum RetreatType {

    NORMAL(0,  "正常退住"),
    DECEASED(1,"死亡退住"),
    RESERVED(2,"保留床位");

    private int code;
    private String text;

    RetreatType(int code, String text) { this.code = code; this.text = text; }

    public int getCode() { return code; }
    public String getText() { return text; }

    public static String textOf(int code) {
        for (RetreatType t : values()) {
            if (t.code == code) return t.text;
        }
        return "未知";
    }
}
