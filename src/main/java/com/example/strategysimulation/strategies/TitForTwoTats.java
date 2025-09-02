package com.example.strategysimulation.strategies;
import com.example.strategysimulation.models.Interaction;
import java.util.List;

public class TitForTwoTats implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        if (history.size() < 2) return true;
        Interaction lastInteraction = history.get(history.size() - 1);
        Interaction secondLastInteraction = history.get(history.size() - 2);
        return !(lastInteraction.getDecisionB() == false && secondLastInteraction.getDecisionB() == false);
    }
}