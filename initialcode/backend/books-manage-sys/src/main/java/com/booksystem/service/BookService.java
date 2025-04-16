package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.entity.Book;
import com.booksystem.pojo.vo.BookVO;
import com.booksystem.pojo.vo.ChartVO;

import java.util.List;

/**
 * 書籍業務邏輯接口
 */
public interface BookService {

    Result<Void> save(Book book);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(Book book);

    Result<List<BookVO>> query(BookQueryDto bookQueryDto);

    Result<List<ChartVO>> daysQuery(Integer day);
}
