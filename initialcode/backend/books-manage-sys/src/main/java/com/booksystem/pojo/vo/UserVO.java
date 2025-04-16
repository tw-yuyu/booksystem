package com.booksystem.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用戶VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    /**
     * 用戶編號
     */
    private Integer id;

    /**
     * 用戶帳號
     */
    private String userAccount;

    /**
     * 用戶暱稱
     */
    private String userName;

    /**
     * 用戶頭像
     */
    private String userAvatar;

    /**
     * 用戶郵箱
     */
    private String userEmail;

    /**
     * 用戶角色
     */
    private Integer userRole;

    /**
     * 註冊時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}