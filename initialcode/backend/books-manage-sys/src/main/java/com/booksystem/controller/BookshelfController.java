package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookshelfQueryDto;
import com.booksystem.pojo.entity.Bookshelf;
import com.booksystem.service.BookshelfService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書架的 Controller
 */
@RestController
@RequestMapping(value = "/bookshelf")
public class BookshelfController {

    @Resource
    private BookshelfService bookshelfService;

    /**
     * 書架新增
     *
     * @param bookshelf 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody Bookshelf bookshelf) {
        return bookshelfService.save(bookshelf);
    }

    /**
     * 書架删除
     *
     * @param ids 要删除的書架ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return bookshelfService.batchDelete(ids);
    }

    /**
     * 書架修改
     *
     * @param bookshelf 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody Bookshelf bookshelf) {
        return bookshelfService.update(bookshelf);
    }

    /**
     * 書架查詢
     *
     * @param bookshelfQueryDto 查詢參數
     * @return Result<List < BookVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<Bookshelf>> query(@RequestBody BookshelfQueryDto bookshelfQueryDto) {
        return bookshelfService.query(bookshelfQueryDto);
    }

}
