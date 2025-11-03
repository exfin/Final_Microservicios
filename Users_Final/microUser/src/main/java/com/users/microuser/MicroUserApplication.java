package com.users.microuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroUserApplication.class, args);
    }

}
