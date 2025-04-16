package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookSaveQueryDto;
import com.booksystem.pojo.entity.BookSave;
import com.booksystem.pojo.vo.BookSaveVO;
import com.booksystem.service.BookSaveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍收藏的 Controller
 */
@RestController
@RequestMapping(value = "/bookSave")
public class BookSaveController {

    @Resource
    private BookSaveService bookSaveService;

    /**
     * 書籍收藏新增
     *
     * @param bookSave 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody BookSave bookSave) {
        return bookSaveService.save(bookSave);
    }

    /**
     * 書籍收藏删除
     *
     * @param ids 要删除的書籍收藏ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return bookSaveService.batchDelete(ids);
    }

    /**
     * 書籍收藏修改
     *
     * @param bookSave 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody BookSave bookSave) {
        return bookSaveService.update(bookSave);
    }

    /**
     * 書籍收藏查詢
     *
     * @param bookSaveQueryDto 查詢參數
     * @return Result<List < BookVO>> 通用響應
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<BookSaveVO>> queryUser(@RequestBody BookSaveQueryDto bookSaveQueryDto) {
        return bookSaveService.queryUser(bookSaveQueryDto);
    }

}
