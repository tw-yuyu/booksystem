package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookOrderHistoryQueryDto;
import com.booksystem.pojo.entity.BookOrderHistory;
import com.booksystem.pojo.vo.BookOrderHistoryVO;
import com.booksystem.service.BookOrderHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍借閱歷史數據的 Controller
 */
@RestController
@RequestMapping(value = "/bookOrderHistory")
public class BookOrderHistoryController {

    @Resource
    private BookOrderHistoryService bookOrderHistoryService;

    /**
     * 書籍借閱歷史數據新增
     *
     * @param bookOrderHistory 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody BookOrderHistory bookOrderHistory) {
        return bookOrderHistoryService.save(bookOrderHistory);
    }

    /**
     * 書籍借閱歷史數據删除
     *
     * @param ids 要删除的書籍借閱歷史數據ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return bookOrderHistoryService.batchDelete(ids);
    }

    /**
     * 書籍借閱歷史數據修改
     *
     * @param bookOrderHistory 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody BookOrderHistory bookOrderHistory) {
        return bookOrderHistoryService.update(bookOrderHistory);
    }

    /**
     * 書籍借閱歷史數據查询
     *
     * @param bookOrderHistoryQueryDto 查詢參數
     * @return Result<List < BookOrderHistoryVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<BookOrderHistoryVO>> query(@RequestBody BookOrderHistoryQueryDto bookOrderHistoryQueryDto) {
        return bookOrderHistoryService.query(bookOrderHistoryQueryDto);
    }
    /**
     * 查詢用戶的所有借閱紀錄
     *
     * @param bookOrderHistoryQueryDto 查詢參數
     * @return Result<List < BookOrderHistoryVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<BookOrderHistoryVO>> queryUser(@RequestBody BookOrderHistoryQueryDto bookOrderHistoryQueryDto) {
        bookOrderHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());
        return bookOrderHistoryService.query(bookOrderHistoryQueryDto);
    }

}
