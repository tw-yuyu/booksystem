package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.RssNotificationQueryDto;
import com.booksystem.pojo.entity.RssNotification;
import com.booksystem.pojo.vo.RssNotificationVO;

import java.util.List;

/**
 * 訂閱通知業務邏輯接口
 */
public interface RssNotificationService {

    Result<Void> save(RssNotification rssNotification);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(RssNotification rssNotification);

    Result<List<RssNotificationVO>> query(RssNotificationQueryDto rssNotificationQueryDto);

    Result<Void> postsRssNotification(Integer id);

    Result<Void> readDeal();
}
