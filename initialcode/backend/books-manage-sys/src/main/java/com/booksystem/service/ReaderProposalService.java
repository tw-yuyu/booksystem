package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.ReaderProposalQueryDto;
import com.booksystem.pojo.entity.ReaderProposal;
import com.booksystem.pojo.vo.ReaderProposalVO;

import java.util.List;

/**
 * 讀者評論業務邏輯接口
 */
public interface ReaderProposalService {

    Result<Void> save(ReaderProposal readerProposal);

    Result<Void> batchDelete(List<Integer> ids);

    Result<Void> update(ReaderProposal readerProposal);

    Result<List<ReaderProposalVO>> query(ReaderProposalQueryDto readerProposalQueryDto);

}
