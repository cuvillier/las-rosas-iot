package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.domain.model.message.AirEnvironment;
import com.lasrosas.iot.ingestor.domain.model.message.BatteryLevel;
import com.lasrosas.iot.ingestor.domain.model.message.DistanceMeasurement;
import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;

import java.util.ArrayList;
import java.util.List;

public class ElsysGenericDriver {

	public static final int TYPE_TEMP = 0x01; // temp 2 bytes -3276.8°C -->3276.7°C
	public static final int TYPE_RH = 0x02; // Humidity 1 byte  0-100%
	public static final int TYPE_ACC = 0x03; // acceleration 3 bytes X,Y,Z -128 --> 127 +/-63=1G
	public static final int TYPE_LIGHT = 0x04; // Light 2 bytes 0-->65535 Lux
	public static final int TYPE_MOTION = 0x05; // No of motion 1 byte  0-255
	public static final int TYPE_CO2 = 0x06; // Co2 2 bytes 0-65535 ppm
	public static final int TYPE_VDD = 0x07; // VDD 2byte 0-65535mV
	public static final int TYPE_ANALOG1 = 0x08; // VDD 2byte 0-65535mV
	public static final int TYPE_GPS = 0x09; // 3bytes lat 3bytes long binary
	public static final int TYPE_PULSE1 = 0x0A; // 2bytes relative pulse count
	public static final int TYPE_PULSE1_ABS = 0x0B; // 4bytes no 0->0xFFFFFFFF
	public static final int TYPE_EXT_TEMP1 = 0x0C; // 2bytes -3276.5C-->3276.5C
	public static final int TYPE_EXT_DIGITAL = 0x0D; // 1bytes value 1 or 0
	public static final int TYPE_EXT_DISTANCE = 0x0E; // 2bytes distance in mm
	public static final int TYPE_ACC_MOTION = 0x0F; // 1byte number of vibration/motion
	public static final int TYPE_IR_TEMP = 0x10; // 2bytes internal temp 2bytes external temp -3276.5C-->3276.5C
	public static final int TYPE_OCCUPANCY = 0x11; // 1byte data
	public static final int TYPE_WATERLEAK = 0x12; // 1byte data 0-255
	public static final int TYPE_GRIDEYE = 0x13; // 65byte temperature data 1byte ref+64byte external temp
	public static final int TYPE_PRESSURE = 0x14; // 4byte pressure data = hPa)
	public static final int TYPE_SOUND = 0x15; // 2byte sound data = peak/avg)
	public static final int TYPE_PULSE2 = 0x16; // 2bytes 0-->0xFFFF
	public static final int TYPE_PULSE2_ABS = 0x17; // 4bytes no 0->0xFFFFFFFF
	public static final int TYPE_ANALOG2 = 0x18; // 2bytes voltage in mV
	public static final int TYPE_EXT_TEMP2 = 0x19; // 2bytes -3276.5C-->3276.5C
	public static final int TYPE_EXT_DIGITAL2 = 0x1A; // 1bytes value 1 or 0
	public static final int TYPE_EXT_ANALOG_UV = 0x1B; // 4 bytes signed int = uV)
	public static final int TYPE_DEBUG = 0x3D; // 4bytes debug

	private int bin16dec(int bin) {
		var num = bin & 0xFFFF;
		if ((0x8000 & num) != 0)
			num = -(0x010000 - num);
		return num;
	}

	private int bin8dec(int bin) {
		var num = bin & 0xFF;
		if ((0x80 & num) != 0)
			num = -(0x0100 - num);
		return num;
	}

	public BaseMessage decodeUplink(LorawanMessageUplinkRx message)  {
		var data = message.decodeData();
	    var frame = ElsysGenericUplinkFrame.builder().build();
		frame.setOrigin(message);

	    for (int i = 0; i < data.length; i++) {

	        switch (data[i]) {
	        case TYPE_TEMP: //Temperature

	        	var temp = (short)(data[i + 1] << 8) | (data[i + 2]& 0xff);
	            temp = bin16dec(temp);
	            frame.setTemperature(temp / 10.0);
	            i += 2;

	            break;

	        case TYPE_RH: //Humidity

	        	var rh = (data[i + 1]);
	            frame.setHumidity((int)rh);
	            i += 1;
	            break;

	        case TYPE_ACC: //Acceleration

	        	frame.setAccelerationX(bin8dec(data[i + 1]));
	            frame.setAccelerationY(bin8dec(data[i + 2]));
	            frame.setAccelerationZ(bin8dec(data[i + 3]));
	            i += 3;
	            break;

	        case TYPE_LIGHT: //Light

	        	frame.setLight((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_MOTION: //Motion sensor(PIR)
	            frame.setMotion((int)data[i + 1]);
	            i += 1;
	            break;

	        case TYPE_CO2: //CO2
	            frame.setCo2((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_VDD: //Battery level
	            frame.setVdd((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_ANALOG1: //Analog input 1
	            frame.setAnalog1((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_GPS: //gps
	            i++;
	            frame.setGpsLatitude((data[i + 0] | data[i + 1] << 8 | data[i + 2] << 16 | ((data[i + 2] & 0x80) != 0 ? 0xFF << 24 : 0)) / 10000);
	            frame.setGpsLongitude(data[i + 3] | data[i + 4] << 8 | data[i + 5] << 16 | ((data[i + 5] & 0x80) != 0 ? 0xFF << 24 : 0) / 10000);
	            i += 5;
	            break;

	        case TYPE_PULSE1: //Pulse input 1
	            frame.setPulse1((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_PULSE1_ABS: //Pulse input 1 absolute value
	            var pulseAbs = (data[i + 1] << 24) | (data[i + 2] << 16) | (data[i + 3] << 8) | (data[i + 4]);
	            frame.setPulseAbs(pulseAbs);
	            i += 4;
	            break;

	        case TYPE_EXT_TEMP1: //External temp
	            var extTemp = (data[i + 1] << 8) | (data[i + 2]& 0xff);
	            extTemp = bin16dec(extTemp);
	            frame.setExternalTemperature(extTemp / 10.0);
	            i += 2;
	            break;

	        case TYPE_EXT_DIGITAL: //Digital input
	            frame.setDigital((int)data[i + 1]);
	            i += 1;
	            break;

	        case TYPE_EXT_DISTANCE: //Distance sensor input
	            frame.setDistance((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_ACC_MOTION: //Acc motion
	            frame.setAccMotion((int)data[i + 1]);
	            i += 1;
	            break;

	        case TYPE_IR_TEMP: //IR temperature

	        	var iTemp = (data[i + 1] << 8) | (data[i + 2]);
	            iTemp = bin16dec(iTemp);
	            var eTemp = (data[i + 3] << 8) | (data[i + 4]);
	            eTemp = bin16dec(eTemp);
	            frame.setIrInternalTemperature(iTemp / 10.0);
	            frame.setIrExternalTemperature(eTemp / 10.0);
	            i += 4;
	            break;

	        case TYPE_OCCUPANCY: //Body occupancy
	            frame.setOccupancy((int)data[i + 1]);
	            i += 1;
	            break;

	        case TYPE_WATERLEAK: //Water leak
	            frame.setWaterleak((int)data[i + 1]);
	            i += 1;
	            break;

	        case TYPE_GRIDEYE: //Grideye data
	            var ref = data[i+1];
	            i++;
	            var grideye = new double [64];
	            for(var j = 0; j < 64; j++) {
	                grideye[j] = ref + (data[1+i+j] / 10.0);
	            }
	            frame.setGrideye(grideye);
	            i += 64;
	            break;

	        case TYPE_PRESSURE: //External Pressure
	            var pressure = (data[i + 1] << 24) | (data[i + 2] << 16) | (data[i + 3] << 8) | (data[i + 4]);
	            frame.setPressure(pressure / 1000.0);
	            i += 4;
	            break;
	        case TYPE_SOUND: //Sound
	            frame.setSoundPeak((int)data[i + 1]);
	            frame.setSoundAvg((int)data[i + 2]);
	            i += 2;
	            break;

	        case TYPE_PULSE2: //Pulse 2
	            frame.setPulse2((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_PULSE2_ABS: //Pulse input 2 absolute value
	            frame.setPulseAbs2((data[i + 1] << 24) | (data[i + 2] << 16) | (data[i + 3] << 8) | (data[i + 4]));
	            i += 4;
	            break;

	        case TYPE_ANALOG2: //Analog input 2
	            frame.setAnalog2((data[i + 1] << 8) | (data[i + 2]& 0xff));
	            i += 2;
	            break;

	        case TYPE_EXT_TEMP2: //External temp 2
	            var extTemp2 = (data[i + 1] << 8) | (data[i + 2]& 0xff);
	            extTemp2 = bin16dec(extTemp2);
	            frame.setExternalTemperature2(extTemp2 / 10.0);
	            i += 2;
	            break;
	        default: //somthing is wrong with data
	        	throw new RuntimeException("Unknow ERS code " + data[i]);
	        }
	    }

		return frame;
	}

	public List<BaseMessage> normalize(BaseMessage message) {

		if( !(message instanceof ElsysGenericUplinkFrame elsysFrame) )
			throw new RuntimeException("Cannot process this type of message : " + message.getClass());

		BaseMessage normalized = null;

		if(elsysFrame.getTemperature() != null || elsysFrame.getHumidity() != null || elsysFrame.getLight() != null) {
			normalized = AirEnvironment.builder()
					.temperature(elsysFrame.getTemperature())
					.humidity(elsysFrame.getHumidity() * 1.0)
					.light(elsysFrame.getLight() == null? null: elsysFrame.getLight() * 1.0)
					.build();
		}

		if(elsysFrame.getVdd() != null ) {
			normalized = BatteryLevel.from(elsysFrame.getVdd() / 1000.0, 0.0, 3.6);
		}

		if(elsysFrame.getDistance() != null ) {
			normalized = DistanceMeasurement.builder()
					.distance(elsysFrame.getDistance() / 1000.0)
					.build();
		}

		var result = new ArrayList<BaseMessage>();

		if(normalized != null) {
			normalized.setOrigin(message);
			result.add(message);
		}

		return result;
	}
}
