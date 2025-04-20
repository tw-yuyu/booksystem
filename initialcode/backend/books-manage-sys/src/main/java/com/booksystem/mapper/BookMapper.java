package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.entity.Book;
import com.booksystem.pojo.vo.BookVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書籍持久化接口
 */
@Mapper
public interface BookMapper {

    void save(Book book);

    void update(Book book);

    /**
     * 借書時需要考慮庫存是否足夠
     * @Param("bookId") 讓 bookId 變量在 SQL 語句中可以用 #{bookId} 訪問。
     * @Param("borrowNum") 讓 borrowNum 變量可以用 #{borrowNum} 訪問。
     * XML裡resultMap是結果查詢的映射@Param是方法參數的映射
     * @param bookId
     * @param borrowNum
     * @return
     *
     */
    int updateStockWithCheck(@Param("bookId") int bookId, @Param("borrowNum") int borrowNum);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<BookVO> query(BookQueryDto bookQueryDto);

    Integer queryCount(BookQueryDto bookQueryDto);

}
