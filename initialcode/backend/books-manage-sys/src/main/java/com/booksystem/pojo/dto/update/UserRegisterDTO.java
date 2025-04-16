package com.booksystem.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    /**
     * 用戶名
     */
    private String userName;
    /**
     * 帳號
     */
    private String userAccount;
    /**
     * 密碼
     */
    private String userPwd;
    /**
     * 用戶郵箱
     */
    private String userEmail;
    /**
     * 用戶頭像
     */
    private String userAvatar;
}