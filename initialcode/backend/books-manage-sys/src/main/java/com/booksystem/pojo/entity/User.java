package com.booksystem.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用戶實體
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

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
     * 用戶密碼
     */
    private String userPwd;


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
     * 可登錄狀態(0:可用；1：不可用)
     */
    private Boolean isLogin;

    /**
     * 禁言狀態(0:可用；1：不可用)
     */
    private Boolean isWord;

    /**
     * 用戶註冊時間
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}