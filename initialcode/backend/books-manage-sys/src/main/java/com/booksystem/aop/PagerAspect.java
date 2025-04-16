package com.booksystem.aop;

import com.booksystem.pojo.dto.query.base.QueryDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PagerAspect {

    /**
     * 環繞通知，用於處理帶有@Pager註解的方法
     *
     * @param joinPoint 連接點
     * @param pager     註解實例
     * @return 原方法執行的結果
     * @throws Throwable 異常
     */
    @Around("@annotation(pager)")
    public Object handlePageableParams(ProceedingJoinPoint joinPoint, Pager pager) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof QueryDto) {
                QueryDto queryDTO = (QueryDto) arg;
                configPager(queryDTO);
            }
        }
        return joinPoint.proceed(args);
    }

    /**
     * 分頁參數轉換邏輯
     *
     * @param queryDTO 分頁參數DTO
     */
    private void configPager(QueryDto queryDTO) {
        if (queryDTO.getCurrent() != null && queryDTO.getSize() != null) {
            queryDTO.setCurrent((queryDTO.getCurrent() - 1) * queryDTO.getSize());
        }
    }
}
