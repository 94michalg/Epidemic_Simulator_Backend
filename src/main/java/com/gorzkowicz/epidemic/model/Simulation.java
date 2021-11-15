package com.gorzkowicz.epidemic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull
    @Min(1)
    private int population;

    @NotNull
    @Min(1)
    private int infected;

    @NotNull
    private double RValue;

    @NotNull
    private double mortality;

    @NotNull
    @Min(1)
    private int infectedTime;

    @NotNull
    @Min(1)
    private int mortalityTime;

    @NotNull
    @Min(1)
    private int simulationTime;

    @OneToMany(
            mappedBy = "simulation",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<DailyStats> dailyStatsList;

    public Simulation() {
    }

    public Simulation(String name, int population, int infected, double RValue, double mortality, int infectedTime, int mortalityTime, int simulationTime) {
        this.name = name;
        this.population = population;
        this.infected = infected;
        this.RValue = RValue;
        this.mortality = mortality;
        this.infectedTime = infectedTime;
        this.mortalityTime = mortalityTime;
        this.simulationTime = simulationTime;
    }

    public void calculateDailyStats() {

        dailyStatsList = new ArrayList<>();

        //setting first day
        double r = RValue / infectedTime;
        DailyStats dayOne = new DailyStats(1, this);
        int dayOneHealthy = population - infected;

        dayOne.setDailyInfections(infected);
        dayOne.setPi(infected);
        dayOne.setPv(dayOneHealthy);
        dayOne.setPm(0);
        dayOne.setPr(0);
        dailyStatsList.add(dayOne);

        int loopPi = infected;
        int loopPv = dayOneHealthy;
        int loopPm = 0;
        int loopPr = 0;

        DailyStats temp;

        for (int i = 2; i <= simulationTime; i++) {
            temp = new DailyStats(i, this);

            // M people infected Tm days ago dies
            int tempDay = i - mortalityTime;
            if (tempDay >= 1) {
                DailyStats dayInPastForTheKills = dailyStatsList.get(tempDay - 1);
                int toKill = (int) Math.round(mortality * dayInPastForTheKills.getDailyInfections());
                if (toKill > loopPi) { // not enough people to kill
                    toKill = loopPi;
                }
                loopPm += toKill;
                loopPi -= toKill;

                dayInPastForTheKills.changeDailyInfections(-toKill);
            }

            // All infected Ti days ago becomes immune
            tempDay = i - infectedTime;
            if (tempDay >= 1) {
                DailyStats dayInPastForTheConvalescents = dailyStatsList.get(tempDay - 1);
                int newConvalescents = dayInPastForTheConvalescents.getDailyInfections();
                dayInPastForTheConvalescents.setDailyInfections(0);
                loopPr += newConvalescents;
                loopPi -= newConvalescents;
            }

            // Counting number of new infected
            int newInfections = (int) Math.round(r * loopPi);
            if (newInfections > loopPv) { // not enough people to infect
                newInfections = loopPv;
            }
            loopPi += newInfections;
            loopPv -= newInfections;

            if (loopPi + loopPm + loopPr + loopPv != population) {
                System.out.println("SOMETHING WRONG!!!!");
            }

            temp.setDailyInfections(newInfections);
            temp.setPi(loopPi);
            temp.setPv(loopPv);
            temp.setPm(loopPm);
            temp.setPr(loopPr);

            dailyStatsList.add(temp);

            if (population == loopPm + loopPr || loopPi == 0) {
                break;
            }

        }
    }

}
