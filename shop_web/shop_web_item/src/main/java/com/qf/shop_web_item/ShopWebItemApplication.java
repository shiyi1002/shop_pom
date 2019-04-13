package com.qf.shop_web_item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.qf",exclude = DataSourceAutoConfiguration.class)
public class ShopWebItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebItemApplication.class, args);
	}

}
