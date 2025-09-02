package com.example.strategysimulation.strategies;

import com.example.strategysimulation.models.Interaction;

import java.util.List;

public class Prober implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        int size = history.size();
        if (size < 2) {
            return true; // Cooperate on the first two moves
        } else if (size == 2) {
            return false; // Defect on the third move
        } else {
            // After the first three moves, mimic the opponent's last move
            return history.get(size - 1).getDecisionB();
        }
    }
}
