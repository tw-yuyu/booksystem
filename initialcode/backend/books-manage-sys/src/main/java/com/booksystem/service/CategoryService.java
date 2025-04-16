package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.CategoryQueryDto;
import com.booksystem.pojo.entity.Category;

import java.util.List;

/**
 * 書籍類別接口
 */
public interface CategoryService {

    Result<Void> save(Category category);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(Category category);

    Result<List<Category>> query(CategoryQueryDto categoryQueryDto);

}
