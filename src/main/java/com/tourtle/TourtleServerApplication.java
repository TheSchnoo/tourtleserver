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

		String[] stringArray = System.getenv("DATABASE_URL").split("@");
		String credentials = stringArray[0].substring(8);
		return DataSourceBuilder
				.create()
				.username(credentials.split(":")[0])
				.password(credentials.split(":")[1])
				.url("jdbc:mysql://" + stringArray[1])
				.build();
	}
}

