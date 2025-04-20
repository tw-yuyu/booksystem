package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.BookshelfQueryDto;
import com.booksystem.pojo.entity.Bookshelf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書架持久化接口
 */
@Mapper
public interface BookshelfMapper {

    void save(Bookshelf bookshelf);

    void update(Bookshelf bookshelf);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<Bookshelf> query(BookshelfQueryDto bookshelfQueryDto);

    Integer queryCount(BookshelfQueryDto bookshelfQueryDto);

}
