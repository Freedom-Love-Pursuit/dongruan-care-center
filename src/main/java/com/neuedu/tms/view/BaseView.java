package com.neuedu.tms.view;

import com.neuedu.tms.utils.InputUtil;

/**
 * 视图基类 - 提供所有视图共用的工具方法
 */
public abstract class BaseView {

    /**
     * 打印统一格式的标题头
     * @param title 标题文本
     */
    protected static void printHeader(String title) {
        System.out.println();
        System.out.println("============ " + title + " ============");
    }

    /**
     * 打印分隔线
     */
    protected static void printDivider() {
        System.out.println("----------------------------------------------");
    }

    /**
     * 等待用户按回车键返回（复用 InputUtil 的全局 Scanner，避免输入冲突）
     */
    protected static void waitEnter() {
        InputUtil.pressEnter("\n按回车键返回...");
    }
}
