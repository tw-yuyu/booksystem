package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 訂閱通知拓展查詢類
 */

/**
 * 書籍前端傳送進來所包的容器(搜尋)
 * const params = {
 * current: this.currentPage,
 * size: this.pageSize,
 * startTime: startTime,
 * endTime: endTime,
 * ...this.bookQueryDto //將模糊搜尋裡的字串傳進來並放在data() 在這裡讀取傳給後端 什麼都沒傳就查詢全部(bookmapper.xml)
 * };
 *   無參數
 *   {current: 1, size: 7, startTime: null, endTime: null}
 *   有參數
 *   {current: 1, size: 7, startTime: null, endTime: null, name: "人"}
 *   在這決定可以用什麼進SQL搜尋查找
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RssNotificationQueryDto extends QueryDto {
    /**
     * 用戶id去查詢
     */
    private Integer userId;
    /**
     * 用戶是否閱讀去查詢
     */
    private Boolean isRead;
    /**
     * 用通知的內容去查詢
     */
    private String content;

}
