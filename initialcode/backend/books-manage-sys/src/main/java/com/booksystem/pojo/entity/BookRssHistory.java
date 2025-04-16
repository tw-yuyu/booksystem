package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 書籍訂閱實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRssHistory {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 書籍ID
     */
    private Integer bookId ;
    /**
     * 使用者ID
     */
    private Integer userId ;
    /**
     * 訂閱的時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
