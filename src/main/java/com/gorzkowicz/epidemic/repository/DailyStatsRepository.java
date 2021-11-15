package com.gorzkowicz.epidemic.repository;

import com.gorzkowicz.epidemic.model.DailyStats;
import org.springframework.data.repository.CrudRepository;

public interface DailyStatsRepository extends CrudRepository<DailyStats, Long> {
}
