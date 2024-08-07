package com.lasrosas.iot.ingestor.adapters.persisters;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("ut")
@ComponentScan(basePackages = {"com.lasrosas.iot.ingestor.adapters.persisters", "com.lasrosas.iot.ingestor.shared"})
@ContextConfiguration(classes=H2JpaConfig.class)
@AutoConfigurationPackage(basePackages = {"com.lasrosas.iot.ingestor.adapters.persisters", "com.lasrosas.iot.ingestor.shared"})
@Sql("/insert-test-data.sql")
public abstract class PersisterTest {
}
