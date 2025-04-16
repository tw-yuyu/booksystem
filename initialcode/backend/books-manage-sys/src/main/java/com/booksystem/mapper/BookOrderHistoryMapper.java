package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.BookOrderHistoryQueryDto;
import com.booksystem.pojo.entity.BookOrderHistory;
import com.booksystem.pojo.vo.BookOrderHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書籍借閱歷史數據持久化接口
 */
@Mapper
public interface BookOrderHistoryMapper {

    void save(BookOrderHistory bookOrderHistory);

    void update(BookOrderHistory bookOrderHistory);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<BookOrderHistoryVO> query(BookOrderHistoryQueryDto bookOrderHistoryQueryDto);

    Integer queryCount(BookOrderHistoryQueryDto bookOrderHistoryQueryDto);

}
