package com.sws.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan("com.sws.code.mycode.dao")
public class MyApplication {

    @RequestMapping("/")
    public String index(){
        return "Hello SpringBoot";
    }

    public static  void main(String[] args){
        SpringApplication.run(MyApplication.class);
    }
}