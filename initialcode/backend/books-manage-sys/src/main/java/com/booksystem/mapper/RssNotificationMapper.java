package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.RssNotificationQueryDto;
import com.booksystem.pojo.entity.RssNotification;
import com.booksystem.pojo.vo.RssNotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 訂閱通知持久化接口
 */
@Mapper
public interface RssNotificationMapper {

    //在RssNotificationServiceImpl需要通知各個上線通知轉發消息 需要裝成list
    //@Param(value = "list")  MyBatis會把這個參數命名為 list
    void save(@Param(value = "list") List<RssNotification> rssNotificationList);

    void update(RssNotification book);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<RssNotificationVO> query(RssNotificationQueryDto rssNotificationQueryDto);

    Integer queryCount(RssNotificationQueryDto rssNotificationQueryDto);

    int readDeal(@Param(value = "userId") Integer userId);
}
