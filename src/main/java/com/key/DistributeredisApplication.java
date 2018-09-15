package com.key;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfigurationPackage
@MapperScan("com.key.modules.*.mapper")
public class DistributeredisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeredisApplication.class, args);
        System.out.println("thisss");
    }
}
