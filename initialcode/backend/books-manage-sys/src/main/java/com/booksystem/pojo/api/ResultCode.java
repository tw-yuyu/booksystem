package com.booksystem.pojo.api;

/**
 * 請求響應碼
 */
public enum ResultCode {
    /**
     * 請求成功碼
     */
    REQUEST_SUCCESS(200),
    /**
     * 請求失敗碼
     */
    REQUEST_ERROR(400);

    private Integer code;

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    ResultCode(Integer code) {

        this.code = code;
    }
}