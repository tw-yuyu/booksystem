package com.booksystem.service.impl;

import com.booksystem.mapper.BookMapper;
import com.booksystem.mapper.NoticeMapper;
import com.booksystem.mapper.UserMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.BookQueryDto;
import com.booksystem.pojo.dto.query.extend.NoticeQueryDto;
import com.booksystem.pojo.dto.query.extend.UserQueryDto;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.service.ViewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 首頁數據(圓餅圖)
 */
@Service
public class ViewsServiceImpl implements ViewsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private BookMapper bookMapper;

    /**
     * 統計一些系統的基礎數據
     *
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> staticControls() {
        List<ChartVO> chartVOS = new ArrayList<>();
        // 1. 用戶數
        UserQueryDto userQueryDto = new UserQueryDto();
        int userCount = userMapper.queryCount(userQueryDto);
        change(userCount, "存量用戶（個）", chartVOS);
        //公告
        NoticeQueryDto noticeQueryDto = new NoticeQueryDto();
        int noticeCount = noticeMapper.queryCount(noticeQueryDto);
        change(noticeCount, "公告（篇）", chartVOS);
        //公告
        BookQueryDto bookQueryDto = new BookQueryDto();
        int bookCount = bookMapper.queryCount(bookQueryDto);
        change(bookCount, "書籍（本）", chartVOS);
        return ApiResult.success(chartVOS);
        //chartVOS=
        //[ChartVO(name=存量用戶（個）, count=3),
        // ChartVO(name=公告（篇）, count=18),
        // ChartVO(name=書籍（本）, count=3)]

    }

    /**
     * 參數處理
     *
     * @param count    總數目
     * @param name     統計項
     * @param chartVOS 裝它們的集合
     */
    private void change(Integer count, String name, List<ChartVO> chartVOS) {
        ChartVO chartVO = new ChartVO(name, count);
        chartVOS.add(chartVO);
    }


}
