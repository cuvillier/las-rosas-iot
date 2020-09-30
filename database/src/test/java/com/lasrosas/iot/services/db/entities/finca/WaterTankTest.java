package com.lasrosas.iot.services.db.entities.finca;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.lasrosas.iot.services.db.finca.WaterTank;

public class WaterTankTest {

	@Test
	public void computeVolumeReal() {
		var tank = new WaterTank(6, 2.5/2, 0.15);

		tank.setLevel(1.27);
		System.out.println(tank.getVolumeMax());
		System.out.println(tank.getVolume());
		System.out.println(tank.getPercentage() + "%");

		tank.setLevel(0.891);
		var vol2 = tank.getVolume();
		tank.setLevel(0.945);
		var vol1 = tank.getVolume();
		System.out.println(tank.getPercentage() + "%");
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
}
