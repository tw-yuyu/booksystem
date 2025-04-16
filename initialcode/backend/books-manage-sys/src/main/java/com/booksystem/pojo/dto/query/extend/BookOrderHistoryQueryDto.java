package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書籍借閱歷史數據拓展查詢類
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookOrderHistoryQueryDto extends QueryDto {
    /**
     * 用戶的Id去搜尋
     */
    private Integer userId;
    /**
     * 書籍的Id去搜尋
     */
    private Integer bookId;
    /**
     * 是否已經歸還去搜尋
     */
    private Boolean isReturn;

}
