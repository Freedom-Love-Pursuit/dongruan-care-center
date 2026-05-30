package com.neuedu.tms.view;

/**
 * 命令模式接口 - 所有视图菜单项的共同接口
 * 每个实现类代表一个可执行的菜单操作
 */
public interface MenuItem {
    /**
     * 执行菜单项对应的功能
     */
    void execute();
}
