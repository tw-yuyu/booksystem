package com.booksystem.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    /**
     * 帳號
     */
    private String userAccount;
    /**
     * 密碼
     */
    private String userPwd;
}
