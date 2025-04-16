package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.UserOperationLogQueryDto;
import com.booksystem.pojo.entity.UserOperationLog;
import com.booksystem.pojo.vo.UserOperationLogVO;
import com.booksystem.service.UserOperationLogService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 用戶行為的 Controller
 */
@RestController
@RequestMapping(value = "/userOperationLog")
public class UserOperationLogController {

    @Resource
    private UserOperationLogService userOperationLogService;

    /**
     * 用戶行為新增
     *
     * @param userOperationLog 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody UserOperationLog userOperationLog) {
        return userOperationLogService.save(userOperationLog);
    }

    /**
     * 用戶行為删除
     *
     * @param ids 要删除的用戶行為ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return userOperationLogService.batchDelete(ids);
    }

    /**
     * 用戶行為修改
     *
     * @param userOperationLog 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody UserOperationLog userOperationLog) {
        return userOperationLogService.update(userOperationLog);
    }
    /**
     * 用戶行為查詢(所有)
     *
     * @param userOperationLogQueryDto 查詢參數
     * @return Result<List < Category>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<UserOperationLogVO>> query(@RequestBody UserOperationLogQueryDto userOperationLogQueryDto) {
        return userOperationLogService.query(userOperationLogQueryDto);
    }
    /**
     * 用戶行為查詢(僅用戶)
     *
     * @param userOperationLogQueryDto 查詢參數
     * @return Result<List < Category>> 通用響應
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<UserOperationLogVO>> queryUser(@RequestBody UserOperationLogQueryDto userOperationLogQueryDto) {
        userOperationLogQueryDto.setUserId(LocalThreadHolder.getUserId());
        return userOperationLogService.query(userOperationLogQueryDto);
    }



}
