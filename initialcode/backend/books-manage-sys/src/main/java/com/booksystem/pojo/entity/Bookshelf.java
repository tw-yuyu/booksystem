package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 書架實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bookshelf {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 書架所在的樓層
     */
    private String floor;
    /**
     * 書架所屬的區域
     */
    private String area;
    /**
     * 書架名
     */
    private String name;
    /**
     * 書架框架
     */
    private String frame;


    /**
     *  id    int auto_increment comment '書架表主鍵 ID'
     *         primary key,
     *     floor varchar(255) null comment '書架所在的樓層',
     *     area  varchar(255) null comment '書架所屬的區域',
     *     name  varchar(255) null comment '書架名',
     *     frame varchar(255) null comment '書架框架'
     */

}
