package com.booksystem.pojo.vo;

import com.booksystem.pojo.entity.RssNotification;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 訂閱通知數據庫VO
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
 *  回傳Result<List<ReaderProposalVO>>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RssNotificationVO extends RssNotification {
    /**
     * 用戶名
     * (user 表)
     */
    private String userName;
}
