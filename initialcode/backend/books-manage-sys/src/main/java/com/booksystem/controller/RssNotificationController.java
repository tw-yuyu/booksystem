package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.RssNotificationQueryDto;
import com.booksystem.pojo.entity.RssNotification;
import com.booksystem.pojo.vo.RssNotificationVO;
import com.booksystem.service.RssNotificationService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 訂閱通知的 Controller
 */
@RestController
@RequestMapping(value = "/rssNotification")
public class RssNotificationController {

    @Resource
    private RssNotificationService rssNotificationService;

    /**
     * 訂閱通知新增
     *
     * @param rssNotification 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody RssNotification rssNotification) {
        return rssNotificationService.save(rssNotification);
    }

    /**
     * 訂閱通知删除
     *
     * @param ids 要删除的訂閱通知ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return rssNotificationService.batchDelete(ids);
    }

    /**
     * 訂閱通知修改
     *
     * @param rssNotification 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody RssNotification rssNotification) {
        return rssNotificationService.update(rssNotification);
    }
    /**
     * 訂閱通知查詢(全部)
     *
     * @param rssNotificationQueryDto 查詢參數
     * @return Result<List < Category>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<RssNotificationVO>> query(@RequestBody RssNotificationQueryDto rssNotificationQueryDto) {
        return rssNotificationService.query(rssNotificationQueryDto);
    }
    /**
     * 訂閱通知查詢(僅使用者)
     *
     * @param rssNotificationQueryDto 查詢參數
     * @return Result<List < Category>> 通用響應
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<RssNotificationVO>> queryUser(@RequestBody RssNotificationQueryDto rssNotificationQueryDto) {
        rssNotificationQueryDto.setUserId(LocalThreadHolder.getUserId());
        return rssNotificationService.query(rssNotificationQueryDto);
    }

    /**
     * 通知訂閱書籍上線,轉發通知
     * @return Result<List < Category>> 通用響應
     */
    @GetMapping(value = "/postsRssNotification/{id}")
    public Result<Void> postsRssNotification(@PathVariable Integer id) {
        return rssNotificationService.postsRssNotification(id);
    }
    /**
     * 消息全部設為已讀
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/readDeal")
    public Result<Void> readDeal() {
        return rssNotificationService.readDeal() ;
    }



}
