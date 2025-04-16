package com.booksystem.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 用戶身份支持器
 *
 */
public class LocalThreadHolder {

    private static final ThreadLocal<Map<String, Integer>> USER_HOLDER = new ThreadLocal<>();

    /**
     * 設置用戶信息
     *
     * @param userId   用戶ID
     * @param userRole 用戶角色
     */
    public static void setUserId(Integer userId, Integer userRole) {
        Map<String, Integer> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userRole", userRole);
        USER_HOLDER.set(map);
    }

    /**
     * 取出用戶ID
     *
     * @return Integer
     */
    public static Integer getUserId() {
        return USER_HOLDER.get().get("userId");
    }

    /**
     * 取出用戶角色
     *
     * @return Integer
     */
    public static Integer getRoleId() {
        return USER_HOLDER.get().get("userRole");
    }

    /**
     * 防止內存溢出，當前線程結束，釋放資源
     *
     */
    public static void clear() {
        USER_HOLDER.remove();
    }

}
