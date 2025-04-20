package com.booksystem.pojo.vo;

import com.booksystem.pojo.entity.BookSave;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書籍借閱歷史數據庫VO
 */

/**
 *  擴充BookOrderHistory外鍵資料庫的容器
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
public class BookSaveVO extends BookSave {
    /**
     * 書籍名
     * (book 表)
     */
    private String bookName;
    /**
     * 書籍作者
     * (book 表)
     */
    private String author;
    /**
     * 書籍館藏量
     * (book 表)
     */
    private Integer num;
    /**
     * 書架所在的樓層
     * (bookshelf 表)
     */
    private String shelfFloor;
    /**
     * 書架所屬的區域
     * (bookshelf 表)
     */
    private String shelfArea;
    /**
     * 書架框架
     * (bookshelf 表)
     */
    private String shelfName;

    /**
     * 用戶名稱
     * (user 表)
     */
    private String userName;





}
