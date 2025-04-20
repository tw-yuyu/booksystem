package com.booksystem.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口保護註解。
 * 該註解加在接口上，接口自動鑑權，並解析用戶身份信息。
 * 符合用戶身份的，才能使用具體接口功能。
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Protector {
    /**
     * 角色名
     *
     * @return String
     */
    String role() default "";
}
