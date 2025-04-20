package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.BookMapper;
import com.booksystem.mapper.BookOrderHistoryMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookOrderHistoryQueryDto;
import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.entity.BookOrderHistory;
import com.booksystem.pojo.vo.BookOrderHistoryVO;
import com.booksystem.pojo.vo.BookVO;
import com.booksystem.service.BookOrderHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 書籍借閱歷史數據業務邏輯層
 */
@Service
public class BookOrderHistoryServiceImpl implements BookOrderHistoryService {

    @Resource
    private BookOrderHistoryMapper bookOrderHistoryMapper;
    @Resource
    private BookMapper bookMapper;

    /**
     * 書籍借閱歷史數據新增
     *
     * @param bookOrderHistory 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(BookOrderHistory bookOrderHistory) {
        //是不是已經借了這本書
        //開一個新的拓展查詢類
        BookOrderHistoryQueryDto dto = new BookOrderHistoryQueryDto();
        //將UserId放進新的拓展查詢類dto
        dto.setId(LocalThreadHolder.getUserId());
        //將前端傳進來的BookId放新的進拓展查詢類dto
        dto.setBookId(bookOrderHistory.getBookId());
        //查詢跟此UserId和BookId有關的BookOrderHistoryVO物件
        List<BookOrderHistoryVO> BookOrderHistoryVOS = bookOrderHistoryMapper.query(dto);
        //遍歷此BookOrderHistoryVO物件 例如 BOOKID 12  USERID 2 會查出所有跟有關的BookOrderHistoryVO物件
        for (BookOrderHistoryVO bookOrderHistoryVO : BookOrderHistoryVOS) {
            //如果有書籍未歸還就返回 0=未歸還 1=已歸還  0 = false   !false=true 進入if
            if (!bookOrderHistoryVO.getIsReturn()) {
                return ApiResult.error("該書籍未歸還");
            }
        }
        //借的書是不是有足夠數量
        BookQueryDto bookQueryDto = new BookQueryDto();
        //將前端傳進來的BOOKID放進新的BoolK拓展查詢類
        bookQueryDto.setId(bookOrderHistory.getBookId());
        //檢查是否有這個BookId的書籍(是否為空) 如果是就回傳找不到 不是就繼續
        List<BookVO> bookVOS = bookMapper.query(bookQueryDto);
        if (bookVOS.isEmpty()) {
            return ApiResult.error("書籍訊息找不到");
        }
        /**
         * //查詢bookVOS的第一筆資料  雖然BookId查到的只會有一筆但是寫法安全 因為主鍵唯一
         *         BookVO bookVO = bookVOS.get(0);
         *         //查到書籍第一筆資料數量與前端傳進來要借閱的數量相減<0 回傳庫存不足
         *         if (bookVO.getNum() - bookOrderHistory.getDeadlineNum() < 0) {
         *             return ApiResult.error("庫存不足");
         *         }
         *         這樣寫庫存相減會有併發問題 同時間都有人借書時會超賣
         *          bookVO.setNum(bookVO.getNum() - bookOrderHistory.getDeadlineNum());
         *          bookMapper.update(bookVO);
         *
         */
        int borrowNum  = bookOrderHistory.getDeadlineNum();//使用者借閱的數量
        //由資料庫來計算庫存 資料庫有原子單一性(update有加鎖) A使用者修改完 B使用者才能修改  避免不同使用者同時借閱 造成超借 有成功改變資料的話會回傳0
        int affectedRows = bookMapper.updateStockWithCheck(bookOrderHistory.getBookId(), borrowNum);
        //沒成功改變資料的話回傳庫存不足
        if (affectedRows == 0) {
            return ApiResult.error("庫存不足");
        }
        //將目前用戶放進前端傳過來的舊物件bookOrderHistory裡
        bookOrderHistory.setUserId(LocalThreadHolder.getUserId());
        //設置尚未歸還 (借書時要設定未歸還)
        bookOrderHistory.setIsReturn(false);
        //設置借書的現在時間
        bookOrderHistory.setCreateTime(LocalDateTime.now());
        bookOrderHistoryMapper.save(bookOrderHistory);
        return ApiResult.success();
    }

    /**
     * 書籍借閱歷史數據删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        bookOrderHistoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 書籍借閱歷史數據修改
     *
     * @param bookOrderHistory 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(BookOrderHistory bookOrderHistory) {

        bookOrderHistoryMapper.update(bookOrderHistory);
        return ApiResult.success();
    }

    /**
     * 書籍借閱歷史數據查詢
     *
     * @param bookOrderHistoryQueryDto 查詢參數
     * @return Result<List < BookOrderHistoryVO>>
     */
    @Override
    public Result<List<BookOrderHistoryVO>> query(BookOrderHistoryQueryDto bookOrderHistoryQueryDto) {
        List<BookOrderHistoryVO> bookOrderHistoryList = bookOrderHistoryMapper.query(bookOrderHistoryQueryDto);
        Integer totalCount = bookOrderHistoryMapper.queryCount(bookOrderHistoryQueryDto);
        return PageResult.success(bookOrderHistoryList, totalCount);
    }


}
