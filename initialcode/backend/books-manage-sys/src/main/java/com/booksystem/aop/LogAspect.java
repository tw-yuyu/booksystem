package com.booksystem.aop;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.UserOperationLogMapper;
import com.booksystem.pojo.entity.UserOperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日誌接口切面
 */
@Aspect //標記這個類別為切面，表示裡面有切面相關的處理邏輯
@Component //將這個類註冊到Spring的容器內，使之可被掃描並實例化
public class LogAspect {

    @Resource
    private UserOperationLogMapper userOperationLogMapper;
    /**
     * 環繞通知
     * 執行前 --- （目標操作） ---執行後
     * 環繞：兩端攔截
     *
     * @param proceedingJoinPoint 連接點
     * @return Object
     */
    @Around("@annotation(com.booksystem.aop.Log)")
    //環繞通知，在目標方法（被@Log標記的方法）執行前後進行操作
    //@annotation(aop.com.booksystem.Log)表示會攔截所有標記有自訂@Log註解的方法
    public Object auth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 取得被攔截方法的簽名 (signature)，透過此簽名進一步拿到註解
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 獲取註解本身實例
        Log logAnnotation = signature.getMethod().getAnnotation(Log.class);
        //拿到註解裡面的行為描述
        String content = logAnnotation.content();
        //構造消息行為,然後新增到資料庫
        Integer userId = LocalThreadHolder.getUserId();
        LocalDateTime now = LocalDateTime.now();
        UserOperationLog userOperationLog = new UserOperationLog();
        //在bean表裡有宣告鏈式(@Accessors) 所以能用這種方式
        userOperationLog.setUserId(userId).setContent(content).setCreateTime(now);
        List<UserOperationLog> OperationLogList = new ArrayList<>();
        OperationLogList.add(userOperationLog);
        userOperationLogMapper.save(OperationLogList);
        //執行原方法
        Object proceed = proceedingJoinPoint.proceed();
        return proceed;
    }


}
