package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookOrderHistoryQueryDto;
import com.booksystem.pojo.entity.BookOrderHistory;
import com.booksystem.pojo.vo.BookOrderHistoryVO;

import java.util.List;

/**
 * 書籍借閱歷史數據邏輯接口
 */
public interface BookOrderHistoryService {

    Result<Void> save(BookOrderHistory bookOrderHistory);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(BookOrderHistory bookOrderHistory);

    Result<List<BookOrderHistoryVO>> query(BookOrderHistoryQueryDto bookOrderHistoryQueryDto);

}
