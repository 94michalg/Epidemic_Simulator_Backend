package com.gorzkowicz.epidemic.repository;

import com.gorzkowicz.epidemic.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SimulationRepository extends CrudRepository<Simulation, Long> {
}
