package com.booksystem.service.impl;

import com.booksystem.mapper.CategoryMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.CategoryQueryDto;
import com.booksystem.pojo.entity.Category;
import com.booksystem.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍類別業務邏輯層
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 書籍類別新增
     *
     * @param category 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(Category category) {
        categoryMapper.save(category);
        return ApiResult.success();
    }

    /**
     * 書籍類別删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        categoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書籍類別修改
     *
     * @param category 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(Category category) {
        categoryMapper.update(category);
        return ApiResult.success();
    }

    /**
     * 書籍類別查詢
     *
     * @param categoryQueryDto 查詢參數
     * @return Result<List < Category>>
     */
    @Override
    public Result<List<Category>> query(CategoryQueryDto categoryQueryDto) {
        List<Category> categoryList = categoryMapper.query(categoryQueryDto);
        Integer totalCount = categoryMapper.queryCount(categoryQueryDto);
        return PageResult.success(categoryList, totalCount);
    }


}
