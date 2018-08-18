package com.key.distributeredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfigurationPackage
public class DistributeredisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeredisApplication.class, args);
        System.out.println("thisss");
    }
}
