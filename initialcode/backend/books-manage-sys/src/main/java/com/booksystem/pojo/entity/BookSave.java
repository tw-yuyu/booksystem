package com.booksystem.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 書籍收藏類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSave {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 書籍ID
     */
    private Integer bookId;
    /**
     * 用戶ID
     */
    private Integer userId;

}
