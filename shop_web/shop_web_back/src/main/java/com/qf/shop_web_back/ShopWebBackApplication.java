package com.qf.shop_web_back;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.qf",exclude = DataSourceAutoConfiguration.class)
@Import(FdfsClientConfig.class)
public class ShopWebBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopWebBackApplication.class, args);
	}

}
