package com.booksystem.pojo.vo;

import com.booksystem.pojo.entity.BookOrderHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書籍借閱歷史數據庫VO
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
 *  回傳Result<List<BookOrderHistoryVO>>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookOrderHistoryVO extends BookOrderHistory {
    /**
     * 用戶名稱
     * (user 表)
     */
    private String userName;
    /**
     * 書籍名稱
     * (book 表)
     */
    private String bookName;




    /**
     * (
     *     id           int auto_increment comment '圖書預約歷史 ID'
     *         primary key,
     *     book_id      int      null comment '圖書 ID',
     *     user_id      int      null comment '用戶 ID',
     *     deadline_num int      null comment '借書的數量',
     *     is_return    tinyint  null comment '是否已經歸還',
     *     return_time  date     null comment '歸還時間',
     *     create_time  datetime null comment '借書時間'
     * )
     */

}
