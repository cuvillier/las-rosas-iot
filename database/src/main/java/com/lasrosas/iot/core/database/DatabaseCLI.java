package com.lasrosas.iot.core.database;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiverFromThing;
import com.lasrosas.iot.core.database.entities.thg.ThingLora;
import com.lasrosas.iot.core.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.core.shared.utils.UtilsConfig;

@SpringBootApplication
@EnableTransactionManagement
@Import({UtilsConfig.class, IOTDatabaseConfig.class})
public class DatabaseCLI {

	public static final Logger log = LoggerFactory.getLogger(DatabaseCLI.class);

	public static final void main(String... args) {
		var context = SpringApplication.run(DatabaseCLI.class, args);
		var bean = context.getBean(DatabaseCLI.class);
		bean.exportThing("thing.json");
	}

	@Autowired
	private DigitalTwinRepo twinRepo;

	@Autowired
	private Gson gson;

	@Transactional
	public void exportThing(String file) {
		var sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);

		for(var twin : twinRepo.findAll()) {
			writer.format("-").println();
			writer.format("  name: %s", twin.getName()).println();
			writer.format("  type: %s", twin.getType().getName()).println();
			writer.format("  receivers:").println();
			for(var receiver: twin.getReceivers()) {
				writer.format("    -").println();
				writer.format("      sensor: %s", receiver.getSensor()).println();
				writer.format("      reactor: %s", receiver.getType().getReactorType().getBean()).println();
				writer.format("      schema: %s", receiver.getType().getSchema().orElse("")).println();
				writer.format("      role: %s", receiver.getType().getRole()).println();

				if( receiver instanceof TwinReactorReceiverFromThing) {
					var thingReceiver = (TwinReactorReceiverFromThing)receiver;
					var thing = thingReceiver.getThing();

					writer.format("      thing:").println();
					writer.format("        manufacturer: %s", thing.getType().getManufacturer()).println();
					writer.format("        model: %s", thing.getType().getModel()).println();

					if(thing instanceof ThingLora) {
						var thgLora = (ThingLora)thing;
						writer.format("        deveui: %s", thgLora.getDeveui()).println();
					}
				}
			}
		}
		
		System.out.println(sw.getBuffer());
	}

	public void importThing(String file, boolean replace) {
		
	}
}
