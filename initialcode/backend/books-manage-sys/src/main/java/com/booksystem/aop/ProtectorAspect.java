package com.booksystem.aop;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.em.RoleEnum;
import com.booksystem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 接口鑑權保護切面
 */
@Aspect
@Component
public class ProtectorAspect {

    /**
     * 環繞通知
     * 執行前 --- （目標操作） ---執行後
     * 環繞：兩端攔截
     *
     * @param proceedingJoinPoint 連接點
     * @return Object
     */
    @Around("@annotation(com.booksystem.aop.Protector)")
    public Object auth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            return ApiResult.error("身份認證失敗，請先登錄");
        }
        Claims claims = JwtUtil.fromToken(token);
        if (claims == null) {
            return ApiResult.error("身份認證失敗，請先登錄");
        }
        Integer userId = claims.get("id", Integer.class);
        Integer roleId = claims.get("role", Integer.class);
        // 獲取被攔截方法的簽名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 獲取方法上的@Protector註解實例
        Protector protectorAnnotation = signature.getMethod().getAnnotation(Protector.class);
        if (protectorAnnotation == null) {
            return ApiResult.error("身份認證失敗，請先登錄");
        }
        String role = protectorAnnotation.role();
        // 驗證用戶角色
        if (!"".equals(role)) {
            if (!Objects.equals(RoleEnum.ROLE(Math.toIntExact(roleId)), role)) {
                return ApiResult.error("無操作權限");
            }
        }
        // 放在 ThreadLocal裏面，當前線程都可用
        LocalThreadHolder.setUserId(userId, roleId);
        Object result = proceedingJoinPoint.proceed();
        // 請求結束，釋放資源
        LocalThreadHolder.clear();
        return result;
    }


}