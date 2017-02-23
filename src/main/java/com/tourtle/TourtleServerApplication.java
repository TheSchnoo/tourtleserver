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

		String[] stringArray = System.getenv("CLEARDB_DATABASE_URL").substring(8).split("@");

		return DataSourceBuilder
				.create()
				.driverClassName("com.mysql.jdbc.Driver")
				.username(stringArray[0].split(":")[0])
				.password(stringArray[0].split(":")[1])
				.url("jdbc:mysql://" + stringArray[1])
				.build();
	}

}

