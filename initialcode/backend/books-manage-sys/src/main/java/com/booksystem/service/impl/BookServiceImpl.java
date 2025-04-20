package com.booksystem.service.impl;

import com.booksystem.mapper.BookMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.base.QueryDto;
import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.entity.Book;
import com.booksystem.pojo.vo.BookVO;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.service.BookService;
import com.booksystem.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 書籍業務邏輯層
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookMapper bookMapper;

    /**
     * 書籍新增
     *
     * @param book 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(Book book) {
        //如果前端沒有設置就將它設為false
        if(book.getIsPlanBuy() == null){
            book.setIsPlanBuy(false);
        }
        book.setCreateTime(LocalDateTime.now());
        //預購書籍時 需要設置預購的時間
        bookMapper.save(book);
        return ApiResult.success();
    }

    /**
     * 書籍删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書籍修改
     *
     * @param book 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(Book book) {
        bookMapper.update(book);
        return ApiResult.success();
    }

    /**
     * 書籍查询
     *
     * @param bookQueryDto 查詢參數
     * @return Result<List < BookVO>>
     */
    @Override
    public Result<List<BookVO>> query(BookQueryDto bookQueryDto) {
        List<BookVO> bookList = bookMapper.query(bookQueryDto);
        Integer totalCount = bookMapper.queryCount(bookQueryDto);
        return PageResult.success(bookList, totalCount);
    }

    /**
     * 統計某個時間段書籍庫存數據
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        BookQueryDto bookQueryDto = new BookQueryDto();
        bookQueryDto.setStartTime(queryDto.getStartTime());
        bookQueryDto.setEndTime(queryDto.getEndTime());
        List<BookVO> userList = bookMapper.query(bookQueryDto);
        List<LocalDateTime> localDateTimes = userList.stream().map(BookVO::getCreateTime).collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(day, localDateTimes);
        return ApiResult.success(chartVOS);
    }


}
