package com.github.igorsergei4.sagademo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.igorsergei4.sagademo.order",
        "com.github.igorsergei4.sagademo.common"
})
@EnableJpaRepositories(basePackages = {
        "com.github.igorsergei4.sagademo.order",
        "com.github.igorsergei4.sagademo.common"
})
@EntityScan(basePackages = {
        "com.github.igorsergei4.sagademo.order",
        "com.github.igorsergei4.sagademo.common"
})
@PropertySources({
        @PropertySource("classpath:common.properties")
})
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
