package com.booksystem.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登錄狀態枚舉
 */
@Getter
@AllArgsConstructor
public enum LoginStatusEnum {

    USE(false, "可登錄"),
    BANK_USE(true, "登錄狀態異常");

    /**
     * 編碼
     */
    private final Boolean flag;
    /**
     * 名成
     */
    private final String name;

}
