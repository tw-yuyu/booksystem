package com.booksystem.pojo.dto.query.extend;

import com.booksystem.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 讀者評論拓展查詢類
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReaderProposalQueryDto extends QueryDto {
    /**
     * 文字內容去查詢
     */
    private String content;
    /**
     * 用戶ID去查詢
     */
    private String userId;
    /**
     * 用是否發布去查詢
     */
    private Boolean isPublish;

}
