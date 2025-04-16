package com.booksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.booksystem.mapper")
@SpringBootApplication
public class BookManageSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookManageSysApplication.class, args);
    }
}
