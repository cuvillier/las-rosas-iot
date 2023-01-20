package com.lasrosas.iot.alarm;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.service.api.AlarmService;
import com.lasrosas.iot.alarm.service.api.AlarmServiceConfig;
import com.lasrosas.iot.core.database.entities.SampleData;
import com.lasrosas.iot.core.database.entities.dtw.BaseDatabaseTest;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.repo.ThingLoraRepo;

@ContextConfiguration(classes = { AlarmServiceConfig.class})
public class AlarmServiceTest extends BaseDatabaseTest {
	private static final Logger log = LoggerFactory.getLogger(AlarmServiceTest.class);

	@Autowired
	private ThingLoraRepo thingLoraRepo;
	
	@Autowired
	private AlarmService alarmService;

	@Test
	void testAlarmThing(){
		var CAUSE = "Test Alarm";
		var deveui = SampleData.THING_ELSYS_ERS_DEVEUI;

		var thing = thingLoraRepo.findByDeveui(deveui).get();

		alarmService.clearAlarm(thing, Thing.class, CAUSE);

		Alarm alarm;

		alarm = alarmService.openAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE, AlarmGravity.HIGH);
		Assertions.assertNotNull(alarm);

		alarm = alarmService.openAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE, AlarmGravity.HIGH);
		Assertions.assertNull(alarm);

		alarm = alarmService.ackAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE, AlarmGravity.HIGH);
		Assertions.assertNotNull(alarm);
		alarm = alarmService.ackAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE, AlarmGravity.HIGH);
		Assertions.assertNull(alarm);

		alarm = alarmService.closeAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE);
		Assertions.assertNotNull(alarm);
		alarm = alarmService.closeAlarm(LocalDateTime.now(), thing, Thing.class, CAUSE);
		Assertions.assertNull(alarm);
	}
}
