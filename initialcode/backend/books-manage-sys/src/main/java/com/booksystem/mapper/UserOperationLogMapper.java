package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.UserOperationLogQueryDto;
import com.booksystem.pojo.entity.UserOperationLog;
import com.booksystem.pojo.vo.UserOperationLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用戶行為持久化接口
 */
@Mapper
public interface UserOperationLogMapper {

    //在RssNotificationServiceImpl需要通知各個上線通知轉發消息 需要裝成list
    //@Param(value = "list")  MyBatis會把這個參數命名為 list
    void save(@Param(value = "list") List<UserOperationLog> userOperationLogList);

    void update(UserOperationLog userOperationLog);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<UserOperationLogVO> query(UserOperationLogQueryDto UserOperationLogQueryDto);

    Integer queryCount(UserOperationLogQueryDto UserOperationLogQueryDto);

}
