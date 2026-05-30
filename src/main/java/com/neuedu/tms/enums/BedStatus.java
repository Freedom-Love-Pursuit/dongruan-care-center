package com.neuedu.tms.enums;

/**
 * 床位状态枚举 —— 消除床位魔法数字
 */
public enum BedStatus {

    FREE(1,     "空闲"),
    OCCUPIED(2, "有人"),
    OUT(3,      "外出");

    private int code;
    private String text;

    BedStatus(int code, String text) { this.code = code; this.text = text; }

    public int getCode() { return code; }
    public String getText() { return text; }

    public static String textOf(int code) {
        for (BedStatus s : values()) {
            if (s.code == code) return s.text;
        }
        return "未知";
    }
}
