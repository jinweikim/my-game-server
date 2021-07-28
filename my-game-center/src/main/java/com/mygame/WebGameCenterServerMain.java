package com.mygame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.mygame"} )
// 服务发现和注册，启动网关后，从 Consul 获取已注册成功的服务信息, 同时也会把自己的服务信息注册到 Consul 服务上面
@EnableMongoRepositories(basePackages = {"com.mygame"})
public class WebGameCenterServerMain {
    public static void main(String[] args) {
        SpringApplication.run(WebGameCenterServerMain.class, args);
    }

}
