package com.booksystem.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
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
}