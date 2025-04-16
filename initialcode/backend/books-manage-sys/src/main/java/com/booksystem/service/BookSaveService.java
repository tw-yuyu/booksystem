package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookSaveQueryDto;
import com.booksystem.pojo.entity.BookSave;
import com.booksystem.pojo.vo.BookSaveVO;
import java.util.List;


/**
 * 書籍收藏邏輯接口
 */
public interface BookSaveService {

    Result<Void> save(BookSave bookSave);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(BookSave bookSave);

    Result<List<BookSaveVO>> queryUser(BookSaveQueryDto bookSaveQueryDto);
}
