package com.neuedu.tms.enums;

/**
 * 审核状态枚举 —— 消除审核状态魔法数字
 */
public enum AuditStatus {

    SUBMITTED(0, "已提交"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝");

    private int code;
    private String text;

    AuditStatus(int code, String text) { this.code = code; this.text = text; }

    public int getCode() { return code; }
    public String getText() { return text; }

    public static String textOf(int code) {
        for (AuditStatus s : values()) {
            if (s.code == code) return s.text;
        }
        return "未知";
    }
}
