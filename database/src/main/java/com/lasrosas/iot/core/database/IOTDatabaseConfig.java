package com.lasrosas.iot.core.database;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "spring.datasource")
@Validated
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.lasrosas.iot")
@EntityScan(basePackages = "com.lasrosas.iot")
public class IOTDatabaseConfig {

	@NotNull
	private String url;

	@NotNull
	private String username;

	@NotNull
	private String password;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(org.postgresql.Driver.class.getName());
		dataSource.setUrl(url);

		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
