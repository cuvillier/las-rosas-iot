package com.lasrosas.iot.services.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.lasrosas.iot.services.utils.ByteParser.ByteOrder;

public class ByteParserTest {

	@Test
	public void bit() {
		ByteParser parser = new ByteParser(new byte [] {85 /*01010101*/, 85});
		int [] result = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
		
		int i = 0;
		while(parser.hasMore()) {
			assertEquals(result[i++], parser.bit());
		}

		assertEquals(result.length, i);
	}

	@Test
	public void uint8() {
		byte bytes[] = new byte[256];

		for(int i = 0; i < 256; i++) {
			bytes[i] = (byte)i;
		}

		ByteParser parser = new ByteParser(bytes);
		for(int i = 0; i < 256; i++) {
			assertEquals(i, parser.uint(8));
		}		
	}

	@Test
	public void uint() {
		ByteParser parser = new ByteParser(new byte [] {(byte)206 /* 11001110 */});
		int [] lgr = {2, 3, 1, 2};
		int [] result = {2, 3, 0, 3};

		for(int i = 0; i < lgr.length; i++) {
			assertEquals(result[i], parser.suint(lgr[i], ByteOrder.BI, false));
		}
	}
}
