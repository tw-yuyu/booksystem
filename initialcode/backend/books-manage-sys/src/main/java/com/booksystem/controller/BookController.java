package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.entity.Book;
import com.booksystem.pojo.vo.BookVO;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍的 Controller
 */
@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Resource
    private BookService bookService;

    /**
     * 書籍新增
     *
     * @param book 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody Book book) {
        return bookService.save(book);
    }

    /**
     * 書籍删除
     *
     * @param ids 要删除的書籍ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return bookService.batchDelete(ids);
    }

    /**
     * 書籍修改
     *
     * @param book 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody Book book) {
        return bookService.update(book);
    }

    /**
     * 書籍查询
     *
     * @param bookQueryDto 查詢參數
     * @return Result<List < BookVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<BookVO>> query(@RequestBody BookQueryDto bookQueryDto) {
        return bookService.query(bookQueryDto);
    }

    /**
     * 統計某個時間段書籍庫存數據
     *
     * @return Result<List < ChartVO>> 響應結果
     */
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> query(@PathVariable Integer day) {
        return bookService.daysQuery(day);
    }

}
