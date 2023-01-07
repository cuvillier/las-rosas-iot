
package com.lasrosas.iot.core.database.entities;

/**
 * Sample data used by the unit tests.
 * 
 * @author tibo
 *
 */
public final class SampleData {

	public static String THING_ELSYS_MB7389_DEVEUI = "0100000000000001";
	public static String THING_ELSYS_MB7389_UPLINK1 = "AQBmAmED+PvEBw4zDgEsDwAUAA9mRA==";

	public static String THING_ELSYS_ERS_DEVEUI = "0100000000000002";
	public static String THING_ELSYS_ERS_UPLINK1 = "";

	public static String THING_MFC88_LW13IO_DEVEUI = "0200000000000001";
	public static String THING_MFC88_LW13IO_UPLINK1 = "ARHF3CEAAgcHAQA=";

	public static String THING_ADEUNIS_ARF8180BA_DEVEUI = "0300000000000001";
	public static String THING_ADEUNIS_ARF8180BA_UPLINK1 = "MCAAIQAAAAAAAAA=";

	public static String THING_ADEUNIS_ARF8170BA_DEVEUI = "0300000000000002";
	public static String THING_ADEUNIS_ARF8170BA_UPLINK1 = "";

	public static String THING_DRAGINO_LHT65_DEVEUI = "0400000000000001";
	public static String THING_DRAGINO_LHT65_UPLINK1 = "y8gE2QNfAQSEf/8=";

	public static final String GatewayNaturalId = "TestGateway";
	public static final String GlobaleSpaceName= "GlobalSpace";
	public static final String WATER_TANK_DISTANCE_SENSOR_DEVEUI = "0100000000000010";
	public static final String FRIDGE_TEMPERATURE_SENSOR_DEVEUI = "0400000000000010";
	public static final String FRIDGE_NAME = "TestFridge";
	public static final String MULTISWITCH_SENSOR_DEVEUI= "0200000000000010";

	public static record SampleThingType(String manufacturer, String model) {};

	public static SampleThingType Adeunis_ARF8180BA= new SampleThingType("Adeunis","ARF8180BA");
	public static SampleThingType Elsys_ERS = new SampleThingType("Elsys","ERS");
	public static SampleThingType Elsys_MB7389 = new SampleThingType("Elsys","MB7389");
	public static SampleThingType Adeunis_ARF8170BA = new SampleThingType("Adeunis","ARF8170BA");
	public static SampleThingType MFC88_LW13IO = new SampleThingType("MFC88","LW13IO");
	public static SampleThingType DRAGINO_LHT65 = new SampleThingType("DRAGINO","LHT65");
}
