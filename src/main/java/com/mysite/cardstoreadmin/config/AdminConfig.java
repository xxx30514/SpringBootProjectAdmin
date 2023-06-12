package com.mysite.cardstoreadmin.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@Configuration
public class AdminConfig {
	
	/**
	 *  mybatisplus分頁插件配置
	 * @return
	 */
	@Bean
	public MybatisPlusInterceptor pageInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
		paginationInnerInterceptor.setOverflow(true);
		paginationInnerInterceptor.setMaxLimit(500L);
		mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);	
		return mybatisPlusInterceptor;
	}
	/**
	 * RabbitMQ序列化方式 Json 格式 
	 * @return
	 */
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
