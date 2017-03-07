package com.tourtle;

import com.mchange.v2.c3p0.ComboPooledDataSource;
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

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://" + stringArray[1]);
		cpds.setUser(stringArray[0].split(":")[0]);
		cpds.setPassword(stringArray[0].split(":")[1]);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		cpds.setAcquireRetryAttempts(5);
		cpds.setAcquireRetryDelay(3600);

		return cpds;
	}

}

