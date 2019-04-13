package com.qf.shop_web_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.qf",exclude = DataSourceAutoConfiguration.class)
public class ShopWebSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebSearchApplication.class, args);
	}

}
