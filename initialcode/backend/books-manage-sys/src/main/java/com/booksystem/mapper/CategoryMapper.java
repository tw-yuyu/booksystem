package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.CategoryQueryDto;
import com.booksystem.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書籍類別持久化接口
 */
@Mapper
public interface CategoryMapper {

    void save(Category category);

    void update(Category category);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<Category> query(CategoryQueryDto categoryQueryDto);

    Integer queryCount(CategoryQueryDto categoryQueryDto);

}
