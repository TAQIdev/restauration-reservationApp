package com.clientweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClientWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientWebApplication.class, args);
    }

}
