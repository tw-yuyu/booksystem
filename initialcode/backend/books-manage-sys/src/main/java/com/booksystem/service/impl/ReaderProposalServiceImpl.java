package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.ReaderProposalMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.ReaderProposalQueryDto;
import com.booksystem.pojo.entity.ReaderProposal;
import com.booksystem.pojo.vo.ReaderProposalVO;
import com.booksystem.service.ReaderProposalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 讀者評論業務邏輯層
 */
@Service
public class ReaderProposalServiceImpl implements ReaderProposalService {

    @Resource
    private ReaderProposalMapper readerProposalMapper;

    /**
     * 評論新增
     *
     * @param readerProposal 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> save(ReaderProposal readerProposal) {
        readerProposal.setUserId(LocalThreadHolder.getUserId());
        readerProposal.setCreateTime(LocalDateTime.now());
        readerProposalMapper.save(readerProposal);
        return ApiResult.success();
    }

    /**
     * 評論删除
     *
     * @param ids 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        readerProposalMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 評論修改
     *
     * @param readerProposal 參數
     * @return Result<Void> 通用響應
     */
    @Override
    public Result<Void> update(ReaderProposal readerProposal) {
        //看一下管理員是否有回覆 有回覆就需要更新時間
        if(readerProposal.getRePlyContent()!=null){
            readerProposal.setRePlyTime(LocalDateTime.now());
        }
        readerProposalMapper.update(readerProposal);
        return ApiResult.success();
    }

    /**
     * 評論查询
     *
     * @param readerProposalQueryDto 查詢參數
     * @return Result<List < ReaderProposalVO>>
     */
    @Override
    public Result<List<ReaderProposalVO>> query(ReaderProposalQueryDto readerProposalQueryDto) {
        List<ReaderProposalVO> readerProposalList = readerProposalMapper.query(readerProposalQueryDto);
        Integer totalCount = readerProposalMapper.queryCount(readerProposalQueryDto);
        return PageResult.success(readerProposalList, totalCount);
    }


}
