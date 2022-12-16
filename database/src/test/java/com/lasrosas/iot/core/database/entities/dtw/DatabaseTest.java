package com.lasrosas.iot.database.entities.dtw;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class DatabaseTest extends BaseDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(DatabaseTest.class);

	@Test
	@Transactional
	void connectToTheDatabase() throws Exception {
		var metamodel = em.getMetamodel();
		for(var entity: metamodel.getEntities()) {
			log.info("Entity " + entity.getJavaType().getSimpleName());
			var q = em.createQuery("select c from " + entity.getJavaType().getSimpleName() + " c");
			q.getResultList();
		}
	}
}
