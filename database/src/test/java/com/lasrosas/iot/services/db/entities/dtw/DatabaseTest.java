package com.lasrosas.iot.services.db.entities.dtw;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@EnableAutoConfiguration
public class DatabaseTest {

	@Autowired private DataSource dataSource;
	// @Autowired private EntityManager entityManager;

	  @Test
	  void connectToTheDatabase() throws Exception {
		  try(var ctx = dataSource.getConnection()) {
			  
		  } catch(Exception e) {
			  throw e;
		  }
	  }
}
