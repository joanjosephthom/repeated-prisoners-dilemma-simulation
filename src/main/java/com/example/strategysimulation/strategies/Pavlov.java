package com.example.strategysimulation.strategies;

import com.example.strategysimulation.models.Interaction;

import java.util.List;

public class Pavlov implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        if (history.isEmpty()) {
            return true; // Cooperate on the first move
        } else {
            Interaction lastInteraction = history.get(history.size() - 1);
            return lastInteraction.getPayoffA() >= 3; // Cooperate if the last move was rewarded
        }
    }
}
