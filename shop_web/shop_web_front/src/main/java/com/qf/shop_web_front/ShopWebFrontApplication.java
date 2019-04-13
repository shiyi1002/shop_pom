package com.qf.shop_web_front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.qf")
public class ShopWebFrontApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebFrontApplication.class, args);
	}

}
