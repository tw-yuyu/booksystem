package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.UserQueryDto;
import com.booksystem.pojo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用戶持久化接口
 */
public interface UserMapper {


    /**
     * 用戶訊息新增
     *
     * @param userInsert 用戶訊息
     * @return int 受影響行數
     */
    int insert(User userInsert);

    /**
     * 分頁查詢用戶訊息
     *
     * @param userQueryDto 分頁查詢參數
     * @return List<User>
     */
    List<User> query(UserQueryDto userQueryDto);

    /**
     * 查詢滿足分頁查詢的記錄總數
     *
     * @param userQueryDto 分頁查詢參數
     * @return int 數據總數
     */
    int queryCount(UserQueryDto userQueryDto);

    /**
     * 更新用戶訊息
     *
     * @param user 用戶訊息
     * @return int 受影響行數
     */
    int update(User user);

    /**
     * 批量刪除用戶訊息
     *
     * @param ids 用戶ID集合
     */
    void batchDelete(@Param(value = "ids") List<Integer> ids);

    /**
     * 根據不爲空的查詢訊息查找用戶
     *
     * @param user 參數
     * @return User
     */
    User getByActive(User user);

}