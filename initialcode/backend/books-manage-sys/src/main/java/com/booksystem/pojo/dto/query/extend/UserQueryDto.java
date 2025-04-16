package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用戶查詢DTO參數
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDto extends QueryDto {
    /**
     * 用戶的帳號
     */
    private String userAccount;
    /**
     * 用戶的名稱
     */
    private String userName;
    /**
     * 用戶的郵箱
     */
    private String userEmail;
    /**
     * 用戶的角色
     */
    private Boolean role;
    /**
     * 是否可以登錄
     */
    private Boolean isLogin;
    /**
     * 是否被禁言
     */
    private Boolean isWord;
}