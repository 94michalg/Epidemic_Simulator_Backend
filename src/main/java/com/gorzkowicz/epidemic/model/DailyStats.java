package com.gorzkowicz.epidemic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class DailyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private int day;

    @JsonIgnore
    private int dailyInfections;

    //infected population
    private int Pi;

    //healthy, but infection-vulnerable
    private int Pv;

    //deaths
    private int Pm;

    //healthy, immune
    private int Pr;

    @ManyToOne
    @JoinColumn(name = "simulation_id")
    @JsonIgnore
    private Simulation simulation;

    public DailyStats() {
    }

    public DailyStats(int day, Simulation simulation) {
        this.day = day;
        this.simulation = simulation;
    }

    @Override
    public String toString() {
        return "DailyStats{" +
                "day=" + day +
                ", dailyInfections=" + dailyInfections +
                ", Pi=" + Pi +
                ", Pv=" + Pv +
                ", Pm=" + Pm +
                ", Pr=" + Pr +
                '}';
    }

    public void changeDailyInfections(int n) {
        this.dailyInfections += n;
    }
}
