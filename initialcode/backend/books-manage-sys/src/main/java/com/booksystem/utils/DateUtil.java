package com.booksystem.utils;

import com.booksystem.pojo.dto.query.base.QueryDto;
import com.booksystem.pojo.vo.ChartVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 時間工具類
 */
public class DateUtil {

    /**
     * 構造時間查詢器，即指定的開始時間、結束時間
     *
     * @param days 時間範圍
     * @return PagerDTO
     */
    /**
     *         now.minusDays(days)： 從當前時間往回推 days 天，得到那一天同一時刻的時間。
     *         plusDays(1)： 再往前加一天，這樣的操作是為了定位到「下一天」。
     *         with(LocalTime.of(0, 0))： 將時間部分重置為當天的零點，即凌晨 0:00，得到「下一天的開始時間」。
     *         舉例來說，如果 days = 7
     *         且當前時間為 2025-03-31 14:00：
     *         now.minusDays(7) 得到 2025-03-24 14:00，
     *         再加一天得到 2025-03-25 14:00，
     *         然後重設時間為 00:00 得到 2025-03-25 00:00。
     *         LocalDateTime daysAgoEnd = nextDayStart.minusSeconds(1);
     *         這行將「下一天的開始時間」減去 1 秒，得到前一天的最後一秒。繼續上面的例子：
     *         從 2025-03-25 00:00 減 1 秒，結果就是 2025-03-24 23:59:59。
     *         這個時間即作為查詢的起始時間，也就是從那個時間點開始往後查詢到當前時間。
     */
    public static QueryDto startAndEndTime(Integer days) {
        // 查全部
        if (days == -1) {
            return new QueryDto();
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDayStart = now.minusDays(days).plusDays(1).with(LocalTime.of(0, 0));
        LocalDateTime daysAgoEnd = nextDayStart.minusSeconds(1);
        return QueryDto.builder().startTime(daysAgoEnd).endTime(now).build();
    }

    /**
     * 統計指定天數內的記錄數
     *
     * @param dayRange 往前查多少天
     * @param dates    數據源
     * @return Map<String, Integer>
     */
    /**
     * LocalDate startDate = LocalDate.now().minusDays(dayRange);
     * 取得「今天往前推 dayRange 天」的日期
     * LocalDate.now() 只取日期（沒有時間）
     * 比方今天是 2025-03-31，dayRange = 7 → startDate = 2025-03-24
     * List<ChartVO> chartVOS = new ArrayList<>();
     * 建立一個空的清單，準備把每一天的統計結果放進去
     * for (int offset = 0; offset <= dayRange; offset++) {
     * 用一個 for 迴圈來處理從 startDate 開始的每一天
     * offset = 0 表示從起始那天開始
     * offset <= dayRange 表示包括最後一天（總共 dayRange + 1 天）
     * LocalDate currentDate = startDate.plusDays(offset);
     * 算出目前要處理的那一天
     * 第一圈是 startDate，下一圈就是 startDate + 1 天，以此類推
     * String dateKey = String.format("%02d-%02d", currentDate.getMonthValue(), currentDate.getDayOfMonth());
     * 把這一天轉成圖表用的標籤格式，如 "03-24"、"04-01"
     * %02d 是補零的格式，確保 3 變成 03、9 變成 09，看起來整齊
     * int count = (int) dates.stream()
     *     .filter(dateTime -> dateTime.toLocalDate().equals(currentDate))
     *     .count();
     * 從整個 dates 清單中，過濾出那些是發生在 currentDate 的資料
     * dateTime.toLocalDate() 只取日期來比，不看時間（小時、分）
     * 最後 .count() 算出有幾筆資料是當天發生的，轉成 int
     * if (count != 0) {
     *     chartVOS.add(new ChartVO(dateKey, count));
     * }
     * 只把「有資料的日期」加進統計結果
     * 如果某一天完全沒人註冊（count == 0），就跳過
     * ChartVO 可能長這樣：
     */
    public static List<ChartVO> countDatesWithinRange(Integer dayRange, List<LocalDateTime> dates) {
        LocalDate startDate = LocalDate.now().minusDays(dayRange);//取得「今天往前推 dayRange 天」的日期 今天是：2025/03/31
        List<ChartVO> chartVOS = new ArrayList<>();
        for (int offset = 0; offset <= dayRange; offset++) {
            LocalDate currentDate = startDate.plusDays(offset);//再往前加一天，這樣的操作是為了定位到「下一天」。startDate.plusDays(0)2025-03-24
            String dateKey = String.format("%02d-%02d", currentDate.getMonthValue(), currentDate.getDayOfMonth());
            //String.format("%02d-%02d", 3, 24)
            //左邊補 0 到兩位 用-隔開
            //03-24
            int count = (int) dates.stream().filter(dateTime -> dateTime.toLocalDate().equals(currentDate)).count();
            //統計符合條件的筆數	.count()
            //chartVOS.add(new ChartVO(dateKey, count));
            // 只統計不爲0的數據
            if (count != 0) {
                chartVOS.add(new ChartVO(dateKey, count));
            }
        }
        return chartVOS;
    }
}