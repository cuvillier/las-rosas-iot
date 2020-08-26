package com.lasrosas.iot.services.db.entities.dtw;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes={DatabaseTestConfig.class})
@EnableJpaRepositories(basePackages = {"com.lasrosas.iot.services.db.repo"})
@EntityScan("com.lasrosas.iot.services.db.entities")
public class DatabaseTest {

	@PersistenceContext
	EntityManager em;


	  @Test
	  void connectToTheDatabase() throws Exception {

		  try {
			  var ctx = em.createQuery("SELECT thg FROM Thing thg");
			  
		  } catch(Exception e) {
			  throw e;
		  }
	  }
}
