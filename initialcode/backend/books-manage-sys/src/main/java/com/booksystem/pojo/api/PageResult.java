package com.booksystem.pojo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 通用響應 - 分頁
 *
 * @param <T> 泛型
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResult<T> extends Result<T> {

    /**
     * 分頁響應數據
     */
    private T data;
    /**
     * 符合條件的總記錄數
     */
    private Integer total;

    /**
     * 參數構造
     *
     * @param code 響應碼
     */
    public PageResult(Integer code) {
        super(code, "查詢成功");
    }

    /**
     * 分頁查血結果反饋
     *
     * @param data  數據源
     * @param total 總記錄數
     * @param <T>   泛型
     * @return <T>
     */
    public static <T> Result<T> success(T data, Integer total) {
        PageResult<T> result = new PageResult<>(ResultCode.REQUEST_SUCCESS.getCode());
        result.setData(data);
        result.setTotal(total);
        return result;
    }

}
