package com.gorzkowicz.epidemic.response;

import com.gorzkowicz.epidemic.model.Simulation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> getSimulationWithDailyStats(Simulation simulation, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
            map.put("id", simulation.getId());
            map.put("name", simulation.getName());
            map.put("population", simulation.getPopulation());
            map.put("infected", simulation.getInfected());
            map.put("rvalue", simulation.getRValue());
            map.put("mortality", simulation.getMortality());
            map.put("infectedTime", simulation.getInfectedTime());
            map.put("mortalityTime", simulation.getMortalityTime());
            map.put("simulationTime", simulation.getSimulationTime());
            map.put("dailyStatsList", simulation.getDailyStatsList());

            return new ResponseEntity<>(map, httpStatus);
    }
}