package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 書籍實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 書籍名
     */
    private String name;
    /**
     * 書籍封面
     */
    private String cover;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 書籍的作者
     */
    private String author;
    /**
     * 國際標準書號
     */
    private String isbn;
    /**
     * 館藏數量
     */
    private Integer num;
    /**
     * 書籍簡介
     */
    private String detail;
    /**
     * 書架 ID 外建
     */
    private Integer bookshelfId;
    /**
     * 書籍類別 ID 外鍵
     */
    private Integer categoryId;
    /**
     * 是否計劃購買(0:false , 1:true)
     */
    private Boolean isPlanBuy;
    /**
     * 計畫上架時間
     */
    private LocalDate planBuyTime;
    /**
     * 入庫時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     *     name          varchar(255) null comment '書籍名',
     *     cover         varchar(255) null comment '書籍封面',
     *     publisher     varchar(255) null comment '出版社',
     *     author        varchar(255) null comment '書籍的作者',
     *     isbn          varchar(255) null comment '國際標準書號',
     *     num           int          null comment '館藏數量',
     *     detail        varchar(255) null comment '書籍簡介',
     *     bookshelf_id  int          null comment '書架 ID',
     *     category_id   int          null comment '書籍類別 ID',
     *     is_plan_buy   tinyint      null comment '是否計劃購買',
     *     plan_buy_time date         null comment '計劃購買時間',
     *     create_time   datetime     null comment '入庫時間'
     */

}
