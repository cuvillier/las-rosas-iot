package com.lasrosas.iot.core.flux;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lasrosas.iot.core.database.IOTDatabaseConfig;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@EnableIntegration
@ContextConfiguration(classes = { ReactFluxTestConfig.class, IOTDatabaseConfig.class, UtilsConfig.class})
@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@EntityScan("com.lasrosas.iot")
@ActiveProfiles("dev")
public class ReactFluxTest {
	public static Logger log = LoggerFactory.getLogger(ReactFluxTest.class);

	@Test
	@DirtiesContext
	@Transactional
	public void glop() {
	}
}
