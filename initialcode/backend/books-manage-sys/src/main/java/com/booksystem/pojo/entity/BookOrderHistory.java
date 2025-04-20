package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 書籍借閱歷史數據實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookOrderHistory {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 書籍的Id
     */
    private Integer bookId ;
    /**
     * 用戶的Id
     */
    private Integer userId ;
    /**
     * 借書的數量
     */
    private Integer deadlineNum ;
    /**
     * 是否已經歸還
     */
    private Boolean isReturn ;
    /**
     * 歸還的時間
     */
    private LocalDate returnTime ;
    /**
     * 借書的時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime ;


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
