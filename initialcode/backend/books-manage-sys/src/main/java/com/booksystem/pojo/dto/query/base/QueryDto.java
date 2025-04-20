package com.booksystem.pojo.dto.query.base;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 數據拓展查詢的基礎類,有四項基礎,使用時可隨時拓展
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QueryDto {
    /**
     * 用目前ID查詢
     */
    private Integer id;
    /**
     * 當前是第幾頁
     */
    private Integer current;
    /**
     * 頁面數據有幾條
     */
    private Integer size;
    /**
     * 開始時間
     */
    private LocalDateTime startTime;
    /**
     * 結束時間
     */
    private LocalDateTime endTime;
}

