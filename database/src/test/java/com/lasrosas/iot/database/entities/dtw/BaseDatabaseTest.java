package com.lasrosas.iot.database.entities.dtw;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = { DatabaseTestConfig.class, UtilsConfig.class })
@EnableJpaRepositories(basePackages = { "com.lasrosas.iot" })
@EntityScan("com.lasrosas.iot")
public abstract class BaseDatabaseTest {

	@PersistenceContext
	EntityManager em;

}
