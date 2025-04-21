package com.booksystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private DataSource dataSource;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/db")
    public String testDB() {
        try (Connection conn = dataSource.getConnection()) {
            return "✅ 成功連接資料庫";
        } catch (Exception e) {
            return "❌ 失敗：" + e.getMessage();
        }
    }
}