package com.lasrosas.iot.core.webapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.entities.thg.ThingGateway;
import com.lasrosas.iot.core.database.repo.GatewayRepo;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.webapi.model.WebThingModel;
import com.lasrosas.iot.core.webapi.model.WebThingModel.WebThingSummary;

@RestController
@CrossOrigin
public class ThingController {
	
	@Autowired
	private ThingRepo thingRepo;

	@Autowired
	private GatewayRepo gatewayRepo;

	@RequestMapping(value = "/api/gateways", method = RequestMethod.GET)
	@ResponseBody
	public List<ThingGateway> getGateways() {
		var gateways = gatewayRepo.findAll();
		loadGateway(gateways.toArray(new ThingGateway[0]));
		return gateways;
	}

	@RequestMapping(value = "/api/gateway/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ThingGateway getGateway(@PathVariable long id) {
		var gateway = gatewayRepo.findById(id).orElseThrow();
		loadGateway(gateway);
		return gateway;
	}

	private void loadGateway(ThingGateway ... gateways ) {
		for(var gateway: gateways) {
			gateway.getThings().size();
		}
	}

	@RequestMapping(value = "/api/gateway/{gatewayId}/things", method = RequestMethod.GET)
	@ResponseBody
	public List<Thing> getThingsByGateway(@PathVariable long gatewayId) {
		var things = thingRepo.findByGateway_Techid(gatewayId);
		loadThing(things.toArray(new Thing[0]));
		return things;
	}

	@RequestMapping(value = "/api/things", method = RequestMethod.GET)
	@ResponseBody
	public List<Thing> getThings() {
		var things = thingRepo.findAll();
		loadThing(things.toArray(new Thing[0]));
		return things;
	}

	@RequestMapping(value = "/api/things/websum", method = RequestMethod.GET)
	@ResponseBody
	public List<WebThingSummary> getThingsWebSummary() {
		var things = thingRepo.findAll();
		List<WebThingSummary> result = new ArrayList<>();

		for(var thing: things) {

			result.add(WebThingModel.mapWebThingSummary(thing));
		}

		return result;
	}

	@RequestMapping(value = "/api/thing/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Thing getThing(@PathVariable long id) {
		var thing = thingRepo.findById(id).orElseThrow();
		loadThing(thing);
		return thing;
	}

	private void loadThing(Thing ... things) {
		for(var thing: things) {
			thing.getType().getManufacturer();
			thing.getGateway().getNaturalId();
		}
	}

}
