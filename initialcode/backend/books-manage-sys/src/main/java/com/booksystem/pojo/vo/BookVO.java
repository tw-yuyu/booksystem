package com.booksystem.pojo.vo;

import com.booksystem.pojo.entity.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
 *  回傳Result<List<BookVO>>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookVO  extends  Book  {
    /**
     * 類別名
     * (category 表)
     */
    private String categoryName;
    /**
     * 書架所在的樓層
     * (bookshelf 表)
     */
    private String floor;
    /**
     * 書架所屬的區域
     * (bookshelf 表)
     */
    private String area;
    /**
     * 書架名
     * (bookshelf 表)
     */
    private String bookshelfName;
    /**
     * 書架框架
     * (bookshelf 表)
     */
    private String frame;


/**
 * id    int auto_increment comment '書架表主鍵 ID'
 *         primary key,
 *     floor varchar(255) null comment '書架所在的樓層',
 *     area  varchar(255) null comment '書架所屬的區域',
 *     name  varchar(255) null comment '書架名',
 *     frame varchar(255) null comment '書架框架'
 */


}
