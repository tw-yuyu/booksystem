package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書架名拓展查詢類
 */


@EqualsAndHashCode(callSuper = true)
@Data
public class BookshelfQueryDto extends QueryDto {
    /**
     * 用書架名查詢
     */
    private String name;
    /**
     * 用樓層去查詢
     */
    private String floor;


}
