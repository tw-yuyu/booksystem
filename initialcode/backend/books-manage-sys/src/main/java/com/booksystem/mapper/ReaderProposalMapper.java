package com.booksystem.mapper;

import com.booksystem.pojo.dto.query.extend.ReaderProposalQueryDto;
import com.booksystem.pojo.entity.ReaderProposal;
import com.booksystem.pojo.vo.ReaderProposalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 書架持久化接口
 */
@Mapper
public interface ReaderProposalMapper {

    void save(ReaderProposal readerProposal);

    void update(ReaderProposal readerProposal);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<ReaderProposalVO> query(ReaderProposalQueryDto readerProposalQueryDto);

    Integer queryCount(ReaderProposalQueryDto readerProposalQueryDto);

}
