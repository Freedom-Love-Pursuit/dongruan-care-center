package com.neuedu.tms.utils;

import com.neuedu.tms.pojo.*;

import java.util.Comparator;

/**
 * 排序工具类 —— 集中管理所有实体的排序规则
 * <p>
 * 每新增排序需求，只需在这里加一个 Comparator 常量，
 * 然后在对应 Service 的 list 方法末尾调用 Collections.sort(list, SORT_XXX) 即可。
 */
public final class SortUtil {

    /// 工具类私有构造，禁止实例化
    private SortUtil() {}

    // ======================== 用户 ========================

    /**
     * 按角色排序（管理员在前），同角色按用户名升序
     */
    public static final Comparator<User> BY_ROLE_AND_NAME = Comparator
            .comparingInt(User::getRoleId)
            .thenComparing(User::getUsername, Comparator.nullsLast(String::compareTo));

    /** 按昵称升序 */
    public static final Comparator<User> BY_NICKNAME = Comparator
            .comparing(User::getNickname, Comparator.nullsLast(String::compareTo));

    // ======================== 客户 ========================

    /** 按入住日期降序（最新入住在前） */
    public static final Comparator<Customer> BY_CHECKIN_DATE_DESC = Comparator
            .comparing(Customer::getCheckinDate, Comparator.nullsLast(Comparator.reverseOrder()));

    /** 按姓名升序 */
    public static final Comparator<Customer> BY_NAME = Comparator
            .comparing(Customer::getCustomerName, Comparator.nullsLast(String::compareTo));

    /** 按年龄降序 */
    public static final Comparator<Customer> BY_AGE_DESC = Comparator
            .comparingInt(Customer::getCustomerAge).reversed();

    // ======================== 床位 ========================

    /** 按房间号升序，同房间按床位编号升序 */
    public static final Comparator<Bed> BY_ROOM_AND_BEDNO = Comparator
            .comparingInt(Bed::getRoomNo)
            .thenComparing(Bed::getBedNo, Comparator.nullsLast(String::compareTo));

    // ======================== 护理内容 ========================

    /** 按编号升序 */
    public static final Comparator<NurseContent> BY_SERIAL_NUMBER = Comparator
            .comparing(NurseContent::getSerialNumber, Comparator.nullsLast(String::compareTo));

    // ======================== 护理等级 ========================

    /** 按等级名称升序 */
    public static final Comparator<NurseLevel> BY_LEVEL_NAME = Comparator
            .comparing(NurseLevel::getLevelName, Comparator.nullsLast(String::compareTo));

    // ======================== 护理记录 ========================

    /** 按护理时间降序（最新在前） */
    public static final Comparator<NurseRecord> BY_TIME_DESC = Comparator
            .comparing(NurseRecord::getNursingTime, Comparator.nullsLast(Comparator.reverseOrder()));

    // ======================== 外出记录 ========================

    /**
     * 待审核(0)排最前 → 已通过(1) → 已拒绝(2)，
     * 同审核状态按外出时间降序
     */
    public static final Comparator<Outward> OUTWARD_SORT = Comparator
            .comparingInt(Outward::getAuditStatus)
            .thenComparing(Outward::getOutgoingTime, Comparator.nullsLast(Comparator.reverseOrder()));

    // ======================== 退住记录 ========================

    /**
     * 待审核(0)排最前 → 已通过(1) → 已拒绝(2)，
     * 同审核状态按退住时间降序
     */
    public static final Comparator<Backdown> BACKDOWN_SORT = Comparator
            .comparingInt(Backdown::getAuditStatus)
            .thenComparing(Backdown::getRetreatTime, Comparator.nullsLast(Comparator.reverseOrder()));

    // ======================== 护理等级项目 ========================

    /** 按项目ID升序 */
    public static final Comparator<NurseLevelItem> BY_ITEM_ID = Comparator
            .comparingInt(NurseLevelItem::getItemId);

    // ======================== 菜品 ========================

    /** 按菜品名称升序 */
    public static final Comparator<Food> BY_FOOD_NAME = Comparator
            .comparing(Food::getFoodName, Comparator.nullsLast(String::compareTo));

    // ======================== 膳食安排 ========================

    /** 按餐次(早餐→午餐→晚餐)排序 */
    public static final Comparator<Meal> BY_MEAL_TYPE = Comparator
            .comparingInt(Meal::getMealType);

    // ======================== 床位详情 ========================

    /** 按开始日期降序（最近使用在前） */
    public static final Comparator<BedDetail> BY_START_DATE_DESC = Comparator
            .comparing(BedDetail::getStartDate, Comparator.nullsLast(Comparator.reverseOrder()));
}
