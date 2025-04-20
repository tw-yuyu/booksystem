package com.booksystem.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    // 互動消息（評論被被人回覆、評論被別人點贊）、指標消息、系統通知類
    EVALUATIONS_BY_REPLY(1,"評論"),
    EVALUATIONS_BY_UPVOTE(2,"點贊"),
    DATA_MESSAGE(3,"指標提醒"),
    SYSTEM_INFO(4,"系統通知");

    /**
     * 消息類型
     */
    private final Integer type;

    /**
     * 消息類型的備註
     */
    private final String detail;

}
