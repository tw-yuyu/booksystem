package com.booksystem.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 書籍類別實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 類別名
     */
    private String name;

    /**
     *   id   int auto_increment comment '書籍類別主鍵 ID'
     *         primary key,
     *     name varchar(255) null comment '類別名'
     */

}
