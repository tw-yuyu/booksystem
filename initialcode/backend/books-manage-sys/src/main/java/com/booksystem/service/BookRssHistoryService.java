package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookRssHistoryQueryDto;
import com.booksystem.pojo.entity.BookRssHistory;
import com.booksystem.pojo.vo.BookRssHistoryVO;

import java.util.List;


/**
 * 書籍訂閱邏輯接口
 */
public interface BookRssHistoryService {

    Result<Void> save(BookRssHistory bookRssHistory);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(BookRssHistory bookRssHistory);

    Result<List<BookRssHistoryVO>> query(BookRssHistoryQueryDto bookRssHistoryQueryDto);
}
