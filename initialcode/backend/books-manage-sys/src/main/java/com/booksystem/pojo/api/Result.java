package com.booksystem.pojo.api;

/**
 * 響應基類
 *
 * @param <T>
 */
public class Result<T> {
    /**
     * 響應狀態碼
     */
    private Integer code;
    /**
     * 響應消息
     */
    private String msg;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
