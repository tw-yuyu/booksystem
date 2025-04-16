package com.booksystem.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 禁言狀態枚舉
 */
@Getter
@AllArgsConstructor
public enum WordStatusEnum {

    USE(false, "可用"),
    BANK_USE(true, "禁言狀態");

    /**
     * 狀態
     */
    private final Boolean flag;
    /**
     * 名稱
     */
    private final String name;

}
