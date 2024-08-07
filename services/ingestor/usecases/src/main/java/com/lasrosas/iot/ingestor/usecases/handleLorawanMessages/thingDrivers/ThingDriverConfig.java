package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers;

import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BADriver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8180BADriver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino.DraginoLHT65Driver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys.ElsysErsDriver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys.ElsysGenericDriver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys.ElsysMB7389Driver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.mfc88.MFC88LW13IODriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
public class ThingDriverConfig {

	@Bean
	public AdeunisARF8180BADriver AdenuisARF8180BADriver() {
		return new AdeunisARF8180BADriver();
	}

	@Bean
	public AdeunisARF8170BADriver AdenuisARF8170BADriver() {
		return new AdeunisARF8170BADriver();
	}

	@Bean
	public ElsysGenericDriver ElsysGenericDriver() {
		return new ElsysGenericDriver();
	}

	@Bean
	public ElsysErsDriver ElsysErsDriver() {
		return new ElsysErsDriver();
	}

	@Bean
	public ElsysMB7389Driver ElsysMB7389Driver(ElsysGenericDriver ElsysGenericDriver) {
		return new ElsysMB7389Driver(ElsysGenericDriver);
	}

	@Bean
	public MFC88LW13IODriver MFC88LW13IODriver() {
		return new MFC88LW13IODriver();
	}

	@Bean
	public DraginoLHT65Driver DraginoLHT65Driver() {
		return new DraginoLHT65Driver();
	}

	@Bean
	public ThingDriverManager ThingDriverManager(
			AdeunisARF8180BADriver adenuisARF8180BADriver,
			AdeunisARF8170BADriver adenuisARF8170BADriver,
			ElsysErsDriver elsysErsDriver,
			ElsysMB7389Driver elsysMB7389Driver,
			MFC88LW13IODriver mfc88LW1310Driver,
			DraginoLHT65Driver draginoLHT65Driver) {

		return new ThingDriverManager(adenuisARF8180BADriver, adenuisARF8170BADriver, elsysErsDriver, elsysMB7389Driver, mfc88LW1310Driver, draginoLHT65Driver);
	}
}
