package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookshelfQueryDto;
import com.booksystem.pojo.entity.Bookshelf;

import java.util.List;

/**
 * 書架業務邏輯接口
 */
public interface BookshelfService {

    Result<Void> save(Bookshelf bookshelf);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(Bookshelf bookshelf);

    Result<List<Bookshelf>> query(BookshelfQueryDto bookshelfQueryDto);

}
