package com.lasrosas.iot.ingestor.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteParser {
	public static final Logger log = LoggerFactory.getLogger(ByteParser.class);

	private byte[] bytes;
	private int ibyte = 0;
	private int ibit = 0;

	public ByteParser(byte[] bytes) {
		this.bytes = bytes;
	}

	public boolean hasMore() {
		return ibyte < bytes.length;
	}

	public ByteParser skipBits(int bits) {
		shift(bits);
		return this;
	}

	public int bit() {
		if (bytes == null)
			throw new RuntimeException("byte arry overflow");

		var bit = bytes[ibyte] >> ibit & 1;
		shift(1);
		return bit;
	}

	void checkLgr(int lgr) {
		if (ibit + lgr > 8)
			throw new RuntimeException("Bit overflow ibyte=" + ibyte + ", ibit=" + ibit + ", lgr=" + lgr);
	}

	private void shift(int lgr) {
		ibit += lgr;

		// End of byte
		if (ibit >= 8) {
			ibit = 0;
			ibyte++;
		}
	}

	public enum ByteOrder {
		LI, BI
	}

	public int uint(int lgr) {
		if (lgr > 8)
			throw new RuntimeException("byte order must be specified for int > 8 bits lgr=" + lgr);

		return suint(lgr, ByteOrder.BI, false);
	}

	public int uintBI(int lgr) {
		return suint(lgr, ByteOrder.BI, false);
	}

	public int sint(int lgr) {
		if (lgr > 8)
			throw new RuntimeException("byte order must be specified for int > 8 bits lgr=" + lgr);

		return suint(lgr, ByteOrder.BI, true);
	}

	public int suint(int lgr, ByteOrder indian, boolean signed) {
		return (int)sulong(lgr, indian, signed);
	}

	public long sulong(int lgr, ByteOrder indian, boolean signed) {

		// Ugly code...

		if (lgr > 8 && ibit != 0)
			throw new RuntimeException("int larger than 8 bits must start at bit 0 ibit=" + ibit);

		if (indian == ByteOrder.BI && lgr <= 8) indian = ByteOrder.LI;	// Do not change byte ordering

		if (indian == ByteOrder.BI && ((lgr % 8) != 0))
			throw new RuntimeException("int > 8 bits in BigIndian: lenght must be multiple of 8 lgr=" + 8 + " " + (lgr%8));

		char[] binary = new char[lgr];
		for (int i = 0; i < lgr; i++) {
			binary[binary.length - i - 1] = bit() == 1 ? '1' : '0';
		}

		char [] liBinary = indian == ByteOrder.BI? bigToLittleIndian(binary): binary;

		// Signed integer
		int sign = 1;
		if( signed && liBinary[0] == '1') {
			sign = -1;
			for(int i = 0; i < lgr; i++) {
				liBinary[i] = liBinary[i] == '0'?'1':'0';
			}
		}

		var binaryString = new String(liBinary);
		var result = Long.parseUnsignedLong(binaryString, 2) * sign;

		if( sign < 0 ) result--;
		
		return result;
	}

	public char[] bigToLittleIndian(char[] bi) {
		int nbytes = bi.length / 8;
		char[] li = new char[bi.length];

		for (int b = nbytes - 1; b >= 0; b--) {

			var liOffset = b * 8;
			var biOffset = (nbytes - b - 1) * 8;

			for (int i = 0; i < 8; i++) {
				li[liOffset + i] = bi[biOffset + i];
			}
		}
		
		return li;
	}

	public byte ubyte() {
		if(ibit == 0) {
			var result = bytes[ibyte];
			shift(8);
			return result;
		} else
			return (byte)uint(8);
	}

	public int uint8() {
		return uint(8);
	}

	public int uint16BI() {
		return suint(16, ByteOrder.BI, false);
	}

	public int uint16LI() {
		return suint(16, ByteOrder.LI, false);
	}

	public long uint32BI() {
		return suint(32, ByteOrder.BI, false);
	}

	public long uint32LI() {
		return suint(32, ByteOrder.LI, false);
	}

	public int uint4() {
		return uint(4);
	}

	public int sint16BI() {
		return suint(16, ByteOrder.BI, true);
	}

	public void assertEmpty() {
		if(hasMore() ) throw new RuntimeException("Unparsed bits: lgr=" + bytes.length + ", ibyte=" + ibyte + ", ibit=" + ibit);
	}

	public byte[] bytes() {
		return bytes(bytes.length - ibyte);
	}

	public byte[] bytes(int nbytes) {
		if(ibit != 0) throw new RuntimeException("Not at the begining of a byte");
		
		if(nbytes == 0) return null;

		var result = new byte[nbytes];

		// Make a copy
		for(int i = 0; i < nbytes; i++) result[i] = bytes[i + ibyte];

		ibyte += nbytes;

		return result;
	}
}
