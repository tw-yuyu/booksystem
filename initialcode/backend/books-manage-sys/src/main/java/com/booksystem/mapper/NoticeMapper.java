package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.NoticeQueryDto;
import com.booksystem.pojo.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告持久化接口
 */
@Mapper
public interface NoticeMapper {

    void save(Notice notice);

    void update(Notice notice);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<Notice> query(NoticeQueryDto noticeQueryDto);

    Integer queryCount(NoticeQueryDto noticeQueryDto);

}
