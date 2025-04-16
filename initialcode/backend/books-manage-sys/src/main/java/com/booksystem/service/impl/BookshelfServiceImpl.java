package com.booksystem.service.impl;


import com.booksystem.mapper.BookshelfMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.entity.Bookshelf;
import com.booksystem.service.BookshelfService;
import com.booksystem.pojo.dto.query.extend.BookshelfQueryDto;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 書架業務邏輯層
 */
@Service
public class BookshelfServiceImpl implements BookshelfService {

    @Resource
    private BookshelfMapper bookshelfMapper;

    /**
     * 書架新增
     *
     * @param bookshelf 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(Bookshelf bookshelf) {
        bookshelfMapper.save(bookshelf);
        return ApiResult.success();
    }

    /**
     * 書架删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookshelfMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書架修改
     *
     * @param bookshelf 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(Bookshelf bookshelf) {
       bookshelfMapper.update(bookshelf);
        return ApiResult.success();
    }

    /**
     * 書架查詢
     *
     * @param bookshelfQueryDto 查詢參數
     * @return Result<List < Bookshelf>>
     */
    @Override
    public Result<List<Bookshelf>> query(BookshelfQueryDto bookshelfQueryDto) {
        List<Bookshelf> bookshelfList = bookshelfMapper.query(bookshelfQueryDto);
        Integer totalCount = bookshelfMapper.queryCount(bookshelfQueryDto);
        return PageResult.success(bookshelfList, totalCount);
    }


}
