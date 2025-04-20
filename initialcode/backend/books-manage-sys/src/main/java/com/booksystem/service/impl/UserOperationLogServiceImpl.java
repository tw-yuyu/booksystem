package com.booksystem.service.impl;


import com.booksystem.mapper.UserOperationLogMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.UserOperationLogQueryDto;
import com.booksystem.pojo.entity.UserOperationLog;
import com.booksystem.pojo.vo.UserOperationLogVO;
import com.booksystem.service.UserOperationLogService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用戶行為業務邏輯層
 */
@Service
public class UserOperationLogServiceImpl implements UserOperationLogService {

    @Resource
    private UserOperationLogMapper userOperationLogMapper;


    /**
     * 用戶行為新增
     *
     * @param userOperationLog 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(UserOperationLog userOperationLog) {
        List<UserOperationLog> userOperationLogLists = new ArrayList<>();
        userOperationLog.setCreateTime(LocalDateTime.now());
        userOperationLogLists.add(userOperationLog);
        userOperationLogMapper.save(userOperationLogLists);
        return ApiResult.success();
    }

    /**
     * 用戶行為删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        userOperationLogMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 用戶行為修改
     *
     * @param userOperationLog 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(UserOperationLog userOperationLog) {
        userOperationLogMapper.update(userOperationLog);
        return ApiResult.success();
    }
    /**
     * 用戶行為查詢(所有)
     *
     * @param userOperationLogQueryDto 查詢參數
     * @return Result<List < UserOperationLogVO>>
     */
    @Override
    public Result<List<UserOperationLogVO>> query(UserOperationLogQueryDto userOperationLogQueryDto) {
        List<UserOperationLogVO> UserOperationLogList = userOperationLogMapper.query(userOperationLogQueryDto);
        Integer totalCount = userOperationLogMapper.queryCount(userOperationLogQueryDto);
        return PageResult.success(UserOperationLogList, totalCount);
    }

}
