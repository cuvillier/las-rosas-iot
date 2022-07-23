package com.lasrosas.iot.core.shared.utils;

import org.junit.jupiter.api.Test;

public class BinaryTest {

	@Test
	public void testbin() {
		int b0 = (byte) 0xCB;
		System.out.println(String.format("b0=%02X", b0));

		int b1 = (byte) 0xF6;
		System.out.println(String.format("b1=%02X", b1));

		var v1 = (b0 << 8) & 0xFF00;
		System.out.println(String.format("v1=%02X", v1));

		int v2 = ((b0 & 0xff) << 8) | (b1 & 0xff);
		System.out.println(String.format("v2=%02X", v2));

		int b2 = (v2) & 0x3FFF;
		System.out.println(String.format("%X", b2));
		System.out.println(b2);
	}

	void print(byte value) {

		for(int b = 7; b >= 0 ; b--) {
			var bit = (value >> b) & 1;
			System.out.print(bit);
		}

		System.out.println();
	}

	void print(int value) {

		for(int b = 15; b >= 0 ; b--) {
			var bit = (value >> b) & 1;
			System.out.print(bit);
		}

		System.out.println();
	}
}
