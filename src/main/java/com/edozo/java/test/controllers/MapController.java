package com.edozo.java.test.controllers;

import com.edozo.java.test.model.Map;
import com.edozo.java.test.model.UpsertMap;
import com.edozo.java.test.services.MapService;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/maps")
@AllArgsConstructor
public class MapController {

    MapService mapService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Map> getMaps(@PathParam("address") Optional<String> address) {
        return address
            .map(mapService::findByAddress)
            .orElse(mapService.loadAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getMapById(@PathVariable("id") int id) {
        return mapService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public int createMap(@RequestBody @Valid UpsertMap upsertMap, Errors errors) {
        evaluateErrors(errors);
        return mapService.save(upsertMap);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMap(@PathVariable("id") int id, @RequestBody @Valid UpsertMap upsertMap, Errors errors) {
        evaluateErrors(errors);
        mapService.update(id, upsertMap);
    }

    @DeleteMapping("/{id}")
    public void deleteMap(@PathVariable("id") int id) {
        mapService.delete(id);
    }

    private void evaluateErrors(Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));

            log.info("Bad request received: {}", errorMessage);
            throw new ConstraintViolationException(errorMessage, null);
        }
    }
}
