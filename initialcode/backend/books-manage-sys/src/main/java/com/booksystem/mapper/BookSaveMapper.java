package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.BookSaveQueryDto;
import com.booksystem.pojo.entity.BookSave;
import com.booksystem.pojo.vo.BookSaveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書籍借閱歷史數據持久化接口
 */
@Mapper
public interface BookSaveMapper {

    void save(BookSave bookSave);

    void update(BookSave bookSave);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<BookSaveVO> query(BookSaveQueryDto bookSaveQueryDto);

    Integer queryCount(BookSaveQueryDto bookSaveQueryDto);

}
