package com.mysite.cardstoreadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(basePackages = "com.mysite.cardstoreadmin.mapper")
@SpringBootApplication
public class CardstoreadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardstoreadminApplication.class, args);
	}

}
