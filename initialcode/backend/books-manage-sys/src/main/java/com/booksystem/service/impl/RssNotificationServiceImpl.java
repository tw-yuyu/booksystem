package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.BookMapper;
import com.booksystem.mapper.BookRssHistoryMapper;
import com.booksystem.mapper.RssNotificationMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookRssHistoryQueryDto;
import com.booksystem.pojo.dto.query.extend.RssNotificationQueryDto;
import com.booksystem.pojo.entity.Book;
import com.booksystem.pojo.entity.RssNotification;
import com.booksystem.pojo.vo.BookRssHistoryVO;
import com.booksystem.pojo.vo.RssNotificationVO;
import com.booksystem.service.RssNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂閱通知業務邏輯層
 */
@Service
public class RssNotificationServiceImpl implements RssNotificationService {

    @Resource
    private RssNotificationMapper rssNotificationMapper;
    @Resource
    private BookRssHistoryMapper bookRssHistoryMapper;
    @Resource
    private BookMapper bookMapper;


    /**
     * 訂閱通知新增
     *
     * @param rssNotification 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(RssNotification rssNotification) {
        List<RssNotification> rssNotificationLists = new ArrayList<>();
        rssNotificationLists.add(rssNotification);
        rssNotificationMapper.save(rssNotificationLists);
        return ApiResult.success();
    }

    /**
     * 訂閱通知删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        rssNotificationMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 訂閱通知修改
     *
     * @param rssNotification 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(RssNotification rssNotification) {
        rssNotificationMapper.update(rssNotification);
        return ApiResult.success();
    }
    /**
     * 訂閱通知查询
     *
     * @param rssNotificationQueryDto 查詢參數
     * @return Result<List < RssNotificationVO>>
     */
    @Override
    public Result<List<RssNotificationVO>> query(RssNotificationQueryDto rssNotificationQueryDto) {
        List<RssNotificationVO> rssNotificationList = rssNotificationMapper.query(rssNotificationQueryDto);
        Integer totalCount = rssNotificationMapper.queryCount(rssNotificationQueryDto);
        return PageResult.success(rssNotificationList, totalCount);
    }

    /**
     * 通知訂閱書籍上線,轉發通知
     * @param id 書籍ID
     * @return Result<Void>
     */
    @Override
    public Result<Void> postsRssNotification(Integer id) {
        //前端傳來BOOKID
        BookRssHistoryQueryDto bookRssHistoryqueryDto = new BookRssHistoryQueryDto();
        //將BOOKID設置上搜尋類 並去查詢出這本書 有什麼人訂閱(userid)
        bookRssHistoryqueryDto.setBookId(id);
        List<BookRssHistoryVO> bookRssHistoryList = bookRssHistoryMapper.query(bookRssHistoryqueryDto);
        //如果為空 就是沒有人訂閱 所以也不需要發消息通知
        if(CollectionUtils.isEmpty(bookRssHistoryList)){
            return ApiResult.success();
        }
        //構造消息集合
        List<RssNotification>rssNotifications = new ArrayList<>();
        //bookRssHistoryList裡有一筆資料 就將這筆資料的UserId放進RssNotification裡面
        //最後放進ArrayList集合裡面(ArrayList裡會有很多RssNotification物件)
        for(BookRssHistoryVO bookRssHistoryVO : bookRssHistoryList){
            RssNotification rssNotification = new RssNotification();
            rssNotification.setUserId(bookRssHistoryVO.getUserId());
            rssNotification.setContent("您預定的書籍["+bookRssHistoryVO.getBookName()+"]以上線,歡迎前往借書");
            rssNotification.setIsRead(false);
            rssNotification.setCreateTime(LocalDateTime.now());
            rssNotifications.add(rssNotification);
        }
        /*
        批量發消息
        將裡會有很多RssNotification物件集合放進mapper並儲存進rss_notification資料表裡
        讓queryUser表能搜尋到(queryUser每個使用者只會搜尋到自己userid的資料)
         */
        rssNotificationMapper.save(rssNotifications);
        //將書籍的狀態改變
        Book book = new Book();
        book.setId(id);
        //將預購狀態改成上線狀態
        book.setIsPlanBuy(true);
        //只修改預購狀態
        bookMapper.update(book);
        return ApiResult.success();
    }
    /**
     * 將所有訂閱消息已讀
     * @return Result<Void>
     */
    @Override
    public Result<Void> readDeal() {

        int updatedCount = rssNotificationMapper.readDeal(LocalThreadHolder.getUserId());
        if(updatedCount <= 0){
            return ApiResult.error("訂閱消息已讀沒有修改成功");
        }
        return ApiResult.success();
    }
}
