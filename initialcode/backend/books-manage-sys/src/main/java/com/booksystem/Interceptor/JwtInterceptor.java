package com.booksystem.Interceptor;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.utils.JwtUtil;
import com.alibaba.fastjson2.JSONObject;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * token攔截器，做請求攔截
 * 思路：用戶登錄成功後，會得到後端生成的 token，前端會將token存儲於本地
 * 隨後的接口請求，都會在協議頭帶上token
 * 所有請求執行之前，都會被該攔截器攔截：token校驗通過則正常放行請求，否則直接返回
 *
 */
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 前置攔截
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return boolean true ： 放行；false攔截
     * @throws Exception 異常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestMethod = request.getMethod();
        // 放行預檢請求
        if ("OPTIONS".equals(requestMethod)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        // 登錄及錯誤等請求不做攔截
        if (requestURI.contains("/login") || requestURI.contains("/error") || requestURI.contains("/file") || requestURI.contains("/register")||
                requestURI.contains("/test")) {
            return true;
        }
        String token = request.getHeader("token");
        Claims claims = JwtUtil.fromToken(token);
        // 解析不成功，直接退回！訪問後續資源的可能性都沒有！
        if (claims == null) {
            Result<String> error = ApiResult.error("身份認證異常，請先登錄");
            response.setContentType("application/json;charset=UTF-8");
            Writer stream = response.getWriter();
            // 將失敗信息輸出
            stream.write(JSONObject.toJSONString(error));
            stream.flush();
            stream.close();
            return false;
        }
        Integer userId = claims.get("id", Integer.class);
        Integer roleId = claims.get("role", Integer.class);
        // 將解析出來的用戶ID、用戶角色放置於LocalThread中，當前線程可用
        LocalThreadHolder.setUserId(userId, roleId);
        return true;
    }
}
