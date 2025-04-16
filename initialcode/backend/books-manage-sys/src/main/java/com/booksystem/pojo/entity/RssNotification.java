package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 訂閱通知實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RssNotification {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 使用者ID
     */
    private Integer userId ;
    /**
     * 通知的內容
     */
    private String content ;
    /**
     * 用戶是否已經閱讀
     */
    private Boolean isRead ;
    /**
     * 通知的時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
