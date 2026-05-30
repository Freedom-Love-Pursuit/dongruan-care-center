package com.neuedu.tms.enums;

/**
 * 角色枚举 —— 消除角色ID魔法数字
 */
public enum RoleEnum {

    ADMIN(1, "管理员"),
    NURSE(2, "健康管家");

    private int roleId;
    private String roleName;

    RoleEnum(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }

    /** 根据 roleId 反查枚举 */
    public static RoleEnum fromId(int roleId) {
        for (RoleEnum r : values()) {
            if (r.roleId == roleId) return r;
        }
        return NURSE; // 默认管家
    }
}
