package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookRssHistoryQueryDto;
import com.booksystem.pojo.entity.BookRssHistory;
import com.booksystem.pojo.vo.BookRssHistoryVO;
import com.booksystem.service.BookRssHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍訂閱的 Controller
 */
@RestController
@RequestMapping(value = "/bookRssHistory")
public class BookRssHistoryController {

    @Resource
    private BookRssHistoryService bookRssHistoryService ;

    /**
     * 書籍訂閱新增
     *
     * @param bookRssHistory 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody BookRssHistory bookRssHistory) {
        return bookRssHistoryService.save(bookRssHistory);
    }

    /**
     * 書籍訂閱删除
     *
     * @param ids 要删除的書籍訂閱ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return bookRssHistoryService.batchDelete(ids);
    }

    /**
     * 書籍訂閱修改
     *
     * @param bookRssHistory 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody BookRssHistory bookRssHistory) {
        return bookRssHistoryService.update(bookRssHistory);
    }
    /**
     * 書籍訂閱查詢(全部)
     *
     * @param bookRssHistoryQueryDto 查詢參數
     * @return Result<List < BookVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<BookRssHistoryVO>> query(@RequestBody BookRssHistoryQueryDto bookRssHistoryQueryDto) {
        return bookRssHistoryService.query(bookRssHistoryQueryDto);
    }
    /**
     * 書籍訂閱查詢(使用者)
     *
     * @param bookRssHistoryQueryDto 查詢參數
     * @return Result<List < BookVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<BookRssHistoryVO>> queryUser(@RequestBody BookRssHistoryQueryDto bookRssHistoryQueryDto) {
        bookRssHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());
        return bookRssHistoryService.query(bookRssHistoryQueryDto);
    }

}
