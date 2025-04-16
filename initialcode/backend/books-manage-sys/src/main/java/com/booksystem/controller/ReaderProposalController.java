package com.booksystem.controller;

import com.booksystem.aop.Log;
import com.booksystem.aop.Pager;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.ReaderProposalQueryDto;
import com.booksystem.pojo.entity.ReaderProposal;
import com.booksystem.pojo.vo.ReaderProposalVO;
import com.booksystem.service.ReaderProposalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 讀者評論的 Controller
 */
@RestController
@RequestMapping(value = "/readerproposal")
public class ReaderProposalController {

    @Resource
    private ReaderProposalService readerproposalService;

    /**
     * 評論新增
     *
     * @param readerproposal 新增數據
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/save")
    @Log(content = "像系統提交建議")
    public Result<Void> save(@RequestBody ReaderProposal readerproposal) {

        return readerproposalService.save(readerproposal);
    }

    /**
     * 評論删除
     *
     * @param ids 要删除的評論ID列表
     * @return Result<Void> 通用響應
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Integer> ids) {
        return readerproposalService.batchDelete(ids);
    }

    /**
     * 評論修改
     *
     * @param readerproposal 參數
     * @return Result<Void> 通用響應
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody ReaderProposal readerproposal) {
        return readerproposalService.update(readerproposal);
    }

    /**
     * 評論查詢
     *
     * @param readerproposalQueryDto 查詢參數
     * @return Result<List < readerproposal>> 通用響應
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<ReaderProposalVO>> query(@RequestBody ReaderProposalQueryDto readerproposalQueryDto) {
        return readerproposalService.query(readerproposalQueryDto);
    }

}
