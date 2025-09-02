package com.example.strategysimulation.strategies;

import com.example.strategysimulation.models.Interaction;

import java.util.List;

public class TitForTat implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        if (history.isEmpty()) {
            return true; // Cooperate on the first move
        } else {
            return history.get(history.size() - 1).getDecisionB(); // Mimic the opponent's last move
        }
    }
}
