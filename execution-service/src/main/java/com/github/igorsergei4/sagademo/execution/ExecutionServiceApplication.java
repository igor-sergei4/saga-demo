package com.github.igorsergei4.sagademo.execution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.igorsergei4.sagademo.execution",
        "com.github.igorsergei4.sagademo.common"
})
@EnableJpaRepositories(basePackages = {
        "com.github.igorsergei4.sagademo.execution",
        "com.github.igorsergei4.sagademo.common"
})
@EntityScan(basePackages = {
        "com.github.igorsergei4.sagademo.execution",
        "com.github.igorsergei4.sagademo.common"
})
public class ExecutionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutionServiceApplication.class, args);
	}

}
