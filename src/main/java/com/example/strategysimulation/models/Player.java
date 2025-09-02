package com.example.strategysimulation.models;
import com.example.strategysimulation.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Strategy strategy;
    private final List<Interaction> history;
    private double fitness;

    public Player(Strategy strategy) {
        this.strategy = strategy;
        this.history = new ArrayList<>();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void addInteraction(Interaction interaction) {
        history.add(interaction);
    }

    public List<Interaction> getHistory() {
        return history;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }
}