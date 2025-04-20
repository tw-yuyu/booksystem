package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用戶行為實體類
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//鏈式程式碼
@Accessors(chain = true)
public class UserOperationLog  {
    /**
     * 主鍵
     */
    private Integer id ;
    /**
     * 用戶ID
     */
    private Integer userId ;
    /**
     * 用戶行為描述
     */
    private String content ;
    /**
     * 行為時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
