package com.booksystem.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 書籍數據庫VO
 */

/**
 *  擴充BOOK外鍵資料庫的容器
 *  模糊查詢時前端用BookQueryDto向後端調用@PostMapping(value = "/query")
 *  無參數
 *  {current: 1, size: 7, startTime: null, endTime: null}
 *  有參數
 *  {current: 1, size: 7, startTime: null, endTime: null, name: "人"}
 *  回傳的參數若有外鍵別張表
 *  便在此新增想要回傳的成員變數
 *  回傳Result<List<ChartVO>>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartVO {
    /**
     * 類別名稱
     */
    private String name;
    /**
     * 數據總數
     */
    private Integer count;
}
