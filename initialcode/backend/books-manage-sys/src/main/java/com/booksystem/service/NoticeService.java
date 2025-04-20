package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.NoticeQueryDto;
import com.booksystem.pojo.entity.Notice;

import java.util.List;

/**
 * 公告業務邏輯接口
 */
public interface NoticeService {

    Result<Void> save(Notice notice);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(Notice notice);

    Result<List<Notice>> query(NoticeQueryDto noticeQueryDto);

}
