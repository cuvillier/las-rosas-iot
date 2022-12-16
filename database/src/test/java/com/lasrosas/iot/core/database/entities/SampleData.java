
package com.lasrosas.iot.core.database.entities;

/**
 * Sample data used by the unit tests.
 * 
 * @author tibo
 *
 */
public final class SampleData {
	public static record SampleThingType(long id, String manufacturer, String model) {};

	public static SampleThingType Adeunis_ARF8180BA= new SampleThingType(1l,"Adeunis","ARF8180BA");
	public static SampleThingType Elsys_ERS = new SampleThingType(2,"Elsys","ERS");
	public static SampleThingType Elsys_MB7389 = new SampleThingType(3,"Elsys","MB7389");
	public static SampleThingType Adeunis_ARF8170BA = new SampleThingType(4,"Adeunis","ARF8170BA");
	public static SampleThingType MFC88_LW13IO = new SampleThingType(5,"MFC88","LW13IO");
	public static SampleThingType DRAGINO_LHT65 = new SampleThingType(6,"DRAGINO","LHT65");

	static SampleThingType[] thingTypes = {
		Adeunis_ARF8180BA, Elsys_ERS, Elsys_MB7389, Adeunis_ARF8170BA, MFC88_LW13IO, DRAGINO_LHT65		
	};

	public static record SampleLoraThing(long id, String name, String deveui) {
	};

	public static SampleLoraThing SampleThing1 = new SampleLoraThing(1L, "Temp Adenuis", "0018b2200000093c");
	public static SampleLoraThing SampleThing2 = new SampleLoraThing(2L, "Temp Elsys", "a81758fffe0346aa");
	public static SampleLoraThing SampleThing3 = new SampleLoraThing(3L, "Water tank level sensor", "a81758fffe053159");
	public static SampleLoraThing SampleThing4 = new SampleLoraThing(4L, "Adenuis drycontact", "0018b21000003d4f");
	public static SampleLoraThing SampleThing5 = new SampleLoraThing(5L, "Water pomp switch", "70b3d58ff10184b8");
	public static SampleLoraThing SampleThing6 = new SampleLoraThing(6L, "Test switch", "70b3d58ff10184df");
	public static SampleLoraThing SampleThing7 = new SampleLoraThing(7L, "Dragino temp 1", "a8404114e18446ec");

	static SampleLoraThing[] loraRecords = {
			SampleThing1, SampleThing2, SampleThing3, SampleThing4, SampleThing5, SampleThing6, SampleThing7
	};
}
