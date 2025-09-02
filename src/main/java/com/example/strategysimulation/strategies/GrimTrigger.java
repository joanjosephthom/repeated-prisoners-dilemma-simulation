package com.example.strategysimulation.strategies;

import com.example.strategysimulation.models.Interaction;

import java.util.List;

public class GrimTrigger implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        if (history.isEmpty()) {
            return true; // Cooperate on the first move
        } else {
            for (Interaction interaction : history) {
                if (!interaction.getDecisionB()) {
                    return false; // Defect forever if the opponent has ever defected
                }
            }
            return true; // Cooperate otherwise
        }
    }
}
