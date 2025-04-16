package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.UserOperationLogQueryDto;
import com.booksystem.pojo.entity.UserOperationLog;
import com.booksystem.pojo.vo.UserOperationLogVO;

import java.util.List;

/**
 * 用戶行為業務邏輯接口
 */
public interface UserOperationLogService {

    Result<Void> save(UserOperationLog userOperationLog);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(UserOperationLog userOperationLog);

    Result<List<UserOperationLogVO>> query(UserOperationLogQueryDto userOperationLogQueryDto);

}
