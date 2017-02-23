package com.tourtle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
public class TourtleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourtleServerApplication.class, args);
	}

	@Bean
	@Primary
	public DataSource dataSource() throws Exception {
		return DataSourceBuilder
				.create()
				.driverClassName("com.mysql.jdbc.Driver")
				.username("b6bd158c36251a")
				.password("13f46758")
				.url("jdbc:mysql://us-cdbr-iron-east-04.cleardb.net/heroku_70eefa92f8ba8ee")
				.build();
	}

}

