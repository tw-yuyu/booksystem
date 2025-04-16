package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 書籍類別拓展查詢類
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryQueryDto extends QueryDto {
    /**
     * 書籍類別名去查詢
     */
    private String name;

}
