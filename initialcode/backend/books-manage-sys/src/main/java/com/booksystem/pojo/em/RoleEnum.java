package com.booksystem.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用戶角色枚舉
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN(1, "管理員"),
    USER(2, "用戶");

    /**
     * 角色編碼
     */
    private final Integer role;
    /**
     * 角色名
     */
    private final String name;

    /**
     * 由角色編碼獲取角色名
     *
     * @param role 角色編碼
     * @return String 角色名
     */
    public static String ROLE(Integer role) {
        for (RoleEnum value : RoleEnum.values()) {
            if (value.getRole().equals(role)) {
                return value.name;
            }
        }
        return null;
    }

}
