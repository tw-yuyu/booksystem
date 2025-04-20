package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.BookSaveMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookSaveQueryDto;
import com.booksystem.pojo.entity.BookSave;
import com.booksystem.pojo.vo.BookSaveVO;
import com.booksystem.service.BookSaveService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;

/**
 * 書籍收藏業務邏輯層
 */
@Service
public class BookSaveServiceImpl implements BookSaveService {

    @Resource
    private BookSaveMapper bookSaveMapper;


    /**
     * 書籍收藏新增
     *
     * @param bookSave 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(BookSave bookSave) {
        //如果這個用戶已經收藏過這本書 就無法收藏 BookId和UserId看看有無資料 有資料代表已經收藏過了
        BookSaveQueryDto bookSaveQueryDto = new BookSaveQueryDto();
        bookSaveQueryDto.setBookId(bookSave.getBookId());
        bookSaveQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<BookSaveVO> saveVOS = bookSaveMapper.query(bookSaveQueryDto);
        if(!CollectionUtils.isEmpty(saveVOS)) {
            return ApiResult.error("該本書以收藏,請勿重新收藏");
        }
        //將使用者ID設置上 前端已經帶上BOOKID資訊
        bookSave.setUserId(LocalThreadHolder.getUserId());
        bookSaveMapper.save(bookSave);
        return ApiResult.success();
    }

    /**
     * 書籍收藏删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookSaveMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書籍收藏修改
     *
     * @param bookSave 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(BookSave bookSave ) {
        bookSaveMapper.update(bookSave);
        return ApiResult.success();
    }

    /**
     * 書籍收藏查询
     *
     * @param bookSaveQueryDto 查詢參數
     * @return Result<List < BookSaveVO>>
     */
    @Override
    public Result<List<BookSaveVO>> queryUser(BookSaveQueryDto bookSaveQueryDto) {
        //搜尋只有目前使用者的收藏紀錄
        bookSaveQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<BookSaveVO> bookSaveList = bookSaveMapper.query(bookSaveQueryDto);
        Integer totalCount = bookSaveMapper.queryCount(bookSaveQueryDto);
        return PageResult.success(bookSaveList, totalCount);
    }


}
