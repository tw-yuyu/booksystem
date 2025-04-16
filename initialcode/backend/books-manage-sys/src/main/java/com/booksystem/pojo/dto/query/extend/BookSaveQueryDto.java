package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書籍收藏拓展查詢類
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookSaveQueryDto extends QueryDto {

    /**
     * 使用 用戶ID去搜尋
     */
    private Integer userId;
    /**
     * 使用 用戶ID去搜尋
     * 讓增加收藏時能userId和bookId 確認是否收藏過
     */
    private Integer bookId;



}
