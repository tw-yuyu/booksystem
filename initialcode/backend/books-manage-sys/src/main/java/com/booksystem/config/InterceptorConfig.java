package com.booksystem.config;

import com.booksystem.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**JWT 驗證攔截器
 * API攔截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${my-server.api-context-path}")
    private String API; //從 application.yml 中讀取變數

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 攔截器註冊
        registry.addInterceptor(new JwtInterceptor()) //註冊寫好的攔截器類別
                .addPathPatterns("/**")//所有的路徑都要經過攔截器驗證
                // 放行登錄、註冊請求 這些路徑不攔截，讓使用者可以「先登入、註冊」，才拿到 Token
                .excludePathPatterns(
                        API + "/user/login",
                        API + "/user/register",
                        API + "/file/upload",
                        API + "/file/getFile"
                );
    }
}

