package com.lasrosas.iot.core.database.entities.finca;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import com.lasrosas.iot.core.database.twins.WaterTank;

public class WaterTankTest {

	@Test
	public void computeVolumeReal() {
		var tank = new WaterTank(6, 2.5/2, 0.3);

		tank.setLevel(1.27);
		System.out.println(tank.getVolumeMax());
		System.out.println(tank.getVolume());
		System.out.println(tank.getPercentageFill() + "%");

		tank.setLevel(0.3);
		var vol2 = tank.getVolume();
		tank.setLevel(1.15);
		var vol1 = tank.getVolume();
		System.out.println(tank.getPercentageFill() + "%");
		System.out.println(60*(vol1 - vol2)/56 + " m2/h");
		
		System.out.println("Diff = " + (vol2 - vol1) + " m2");
	}

	@Test
	public void computeVolume() {
		var tank = new WaterTank(6, 2.5/2, 0);

		tank.setLevel(0.0);
		assertEquals(tank.getVolumeMax(), tank.getVolume(), 0.0001);

		tank.setLevel( tank.getRadius());
		assertEquals(tank.getVolumeMax()/2, tank.getVolume(), 0.0001);

		tank.setLevel( tank.getRadius() *2);
		assertEquals(0, tank.getVolume(), 0.0001);
	}

	@Test
	public void computeWaterFlow() {
		var tank = new WaterTank(6, 2.5/2, 0);

		var time = LocalDateTime.now();
		tank.updateLevel(time, 1.0);

		assertEquals(null, tank.getWaterFlow());

		var time2 = time.plus(30, ChronoUnit.MINUTES);

		tank.updateLevel(time2, tank.getLevel() + 0.5);
		assertEquals(-14.89939, tank.getWaterFlow(), 0.0001);
	}
}
