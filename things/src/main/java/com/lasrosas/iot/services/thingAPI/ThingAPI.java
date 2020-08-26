package com.lasrosas.iot.services.thingAPI;

import com.lasrosas.iot.services.db.entities.thg.Thing;

public interface ThingAPI {

	Thing getThingByDevEUI(String deveui, String provider);

}
