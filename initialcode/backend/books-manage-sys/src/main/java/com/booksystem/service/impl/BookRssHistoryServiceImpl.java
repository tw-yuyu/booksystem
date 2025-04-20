package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.BookMapper;
import com.booksystem.mapper.BookRssHistoryMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookRssHistoryQueryDto;
import com.booksystem.pojo.entity.BookRssHistory;
import com.booksystem.pojo.vo.BookRssHistoryVO;
import com.booksystem.service.BookRssHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 書籍訂閱業務邏輯層
 */
@Service
public class BookRssHistoryServiceImpl implements BookRssHistoryService {

    @Resource
    private BookRssHistoryMapper bookRssHistoryMapper;
    @Resource
    private BookMapper bookMapper;


    /**
     * 書籍訂閱新增
     *
     * @param bookRssHistory 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(BookRssHistory bookRssHistory) {
        //如果這個用戶已經訂閱過這本書 就無法訂閱 BookId和UserId看看有無資料 有資料代表已經訂閱過了
        BookRssHistoryQueryDto bookRssHistoryQueryDto = new BookRssHistoryQueryDto();
        bookRssHistoryQueryDto.setBookId(bookRssHistory.getBookId());
        bookRssHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<BookRssHistoryVO> saveVOS = bookRssHistoryMapper.query(bookRssHistoryQueryDto);
        if(!CollectionUtils.isEmpty(saveVOS)) {
            return ApiResult.error("該本書以訂閱,請勿重新訂閱");
        }
        //將使用者ID設置上 前端已經帶上BOOKID資訊
        bookRssHistory.setUserId(LocalThreadHolder.getUserId());
        bookRssHistory.setCreateTime(LocalDateTime.now());
        bookRssHistoryMapper.save(bookRssHistory);
        return ApiResult.success();
    }
    /**
     * 書籍訂閱删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookRssHistoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書籍訂閱修改
     *
     * @param bookRssHistory 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(BookRssHistory bookRssHistory ) {
        bookRssHistoryMapper.update(bookRssHistory);
        return ApiResult.success();
    }

    /**
     * 書籍訂閱查询
     *
     * @param bookRssHistoryQueryDto 查詢參數
     * @return Result<List < BookSaveVO>>book_manage_sys
     */
    @Override
    public Result<List<BookRssHistoryVO>> query(BookRssHistoryQueryDto bookRssHistoryQueryDto) {
        List<BookRssHistoryVO> bookRssHistoryList = bookRssHistoryMapper.query(bookRssHistoryQueryDto);
        Integer totalCount = bookRssHistoryMapper.queryCount(bookRssHistoryQueryDto);
        return PageResult.success(bookRssHistoryList, totalCount);
    }


}
