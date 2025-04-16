package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 讀者評論表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReaderProposal {

    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 反饋者用戶 ID
     */
    private Integer userId;
    /**
     * 反饋的問題內容
     */
    private String content;
    /**
     * 是否公開
     */
    private Boolean isPublish;
    /**
     * 管理者回覆的內容
     */
    private String rePlyContent;
    /**
     * 管理者回覆內容的時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rePlyTime;
    /**
     * 用戶反饋時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;



/**
 * id            int auto_increment comment '讀者反饋 ID'
 *         primary key,
 *     user_id       int          null comment '反饋者用戶 ID',
 *     content       varchar(255) null comment '反饋的問題內容',
 *     is_publish    tinyint      null comment '是否公開',
 *     reply_content varchar(255) null comment '回覆內容',
 *     reply_time    datetime     null comment '回覆時間',
 *     create_time   datetime     null comment '用戶反饋時間'
 */

}
