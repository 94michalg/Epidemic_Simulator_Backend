package com.gorzkowicz.epidemic.controller;

import com.gorzkowicz.epidemic.model.Simulation;
import com.gorzkowicz.epidemic.repository.SimulationRepository;
import com.gorzkowicz.epidemic.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SimulationController {

    @Autowired
    private SimulationRepository simulationRepository;

    @PostMapping("/simulation")
    public ResponseEntity<Object> createTutorial(@Valid @RequestBody Simulation simulation) {
        try {
            Simulation newSimulation = new Simulation(
                    simulation.getName(),
                    simulation.getPopulation(),
                    simulation.getInfected(),
                    simulation.getRValue(),
                    simulation.getMortality(),
                    simulation.getInfectedTime(),
                    simulation.getMortalityTime(),
                    simulation.getSimulationTime()
            );
            newSimulation.calculateDailyStats();

            simulationRepository.save(newSimulation);
            return ResponseHandler.getSimulationWithDailyStats(newSimulation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/simulation")
    public ResponseEntity<List<Simulation>> getAllSimulations() {
        try {
            List<Simulation> list = new ArrayList<>();
            simulationRepository.findAll().forEach(list::add);

            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/simulation/{id}")
    public ResponseEntity<Object> getSimulationById(@PathVariable("id") long id) {
        Optional<Simulation> simulationData = simulationRepository.findById(id);
        return simulationData.map(simulation -> ResponseHandler.getSimulationWithDailyStats(simulation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/simulation/{id}")
    public ResponseEntity<HttpStatus> deleteSimulationById(@PathVariable("id") long id) {
        try {
            simulationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/simulation/{id}")
    public ResponseEntity<Object> updateSimulation(@PathVariable("id") long id, @Valid @RequestBody Simulation simulation) {
        Optional<Simulation> simulationData = simulationRepository.findById(id);

        if (simulationData.isPresent()) {
            Simulation _simulation = simulationData.get();
            _simulation.setName(simulation.getName());
            _simulation.setInfected(simulation.getInfected());
            _simulation.setPopulation(simulation.getPopulation());
            _simulation.setRValue(simulation.getRValue());
            _simulation.setMortality(simulation.getMortality());
            _simulation.setMortalityTime(simulation.getMortalityTime());
            _simulation.setInfectedTime(simulation.getInfectedTime());
            _simulation.setSimulationTime(simulation.getSimulationTime());

            _simulation.calculateDailyStats();
            simulationRepository.save(_simulation);
            return ResponseHandler.getSimulationWithDailyStats(_simulation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
