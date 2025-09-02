package com.example.strategysimulation.strategies;
import com.example.strategysimulation.models.Interaction;
import java.util.List;

public class GenerousTitForTat implements Strategy {
    private static final double FORGIVENESS_PROBABILITY = 0.1;

    @Override
    public boolean decide(List<Interaction> history) {
        if (history.isEmpty()) return true;
        Interaction lastInteraction = history.get(history.size() - 1);
        if (!lastInteraction.getDecisionB() && Math.random() < FORGIVENESS_PROBABILITY) {
            return true;
        }
        return lastInteraction.getDecisionB();
    }
}