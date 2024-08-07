package com.lasrosas.iot.ingestor.adapters.presenters.controllers;

import com.lasrosas.iot.ingestor.adapters.presenters.resources.ThingResource;
import com.lasrosas.iot.ingestor.adapters.presenters.resources.ThingResourceMapper;
import com.lasrosas.iot.ingestor.domain.ports.usecases.UseThingsUseCase;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/thing")
@Slf4j
@NoArgsConstructor
public class ThingPresenter {

    @Autowired
    private UseThingsUseCase useThings;

    @GetMapping("naturalid/{naturalid}")
    public ResponseEntity<ThingResource> getThingLoraByDeveui(@PathVariable(value="naturalid") String naturalid) {

        var thing = useThings.findThingByNaturalid(naturalid);

        // Move this code to a global exception handler
        if(thing.isEmpty())
            return ResponseEntity.notFound().build();

        var resource =  ThingResourceMapper.MAPPER.toThingResource(thing.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping()
    public ResponseEntity<List<ThingResource>> getThings() {

        var things = useThings.getThings();

        var resource = things.stream().map(ThingResourceMapper.MAPPER::toThingResource).toList();
        return ResponseEntity.ok(resource);
    }
}
