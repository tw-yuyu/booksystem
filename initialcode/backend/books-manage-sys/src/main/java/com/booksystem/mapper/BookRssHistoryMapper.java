package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.BookRssHistoryQueryDto;
import com.booksystem.pojo.entity.BookRssHistory;
import com.booksystem.pojo.vo.BookRssHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書籍訂閱持久化接口
 */
@Mapper
public interface BookRssHistoryMapper {

    void save(BookRssHistory bookRssHistory);

    void update(BookRssHistory bookRssHistory);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<BookRssHistoryVO> query(BookRssHistoryQueryDto bookRssHistoryQueryDto);

    Integer queryCount(BookRssHistoryQueryDto bookRssHistoryQueryDto);

}
