package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.CategoryQueryDto;
import com.booksystem.pojo.entity.Category;
import com.booksystem.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍類別的 Controller
 */
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 書籍類別新增
     *
     * @param category 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * 書籍類別删除
     *
     * @param ids 要删除的書籍類別ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return categoryService.batchDelete(ids);
    }

    /**
     * 書籍類別修改
     *
     * @param category 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    /**
     * 書籍類別查詢
     *
     * @param categoryQueryDto 查詢參數
     * @return Result<List < Category>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<Category>> query(@RequestBody CategoryQueryDto categoryQueryDto) {
        return categoryService.query(categoryQueryDto);
    }

}
