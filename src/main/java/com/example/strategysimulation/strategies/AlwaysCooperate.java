package com.example.strategysimulation.strategies;
import com.example.strategysimulation.models.Interaction;
import java.util.List;

public class AlwaysCooperate implements Strategy {
    @Override
    public boolean decide(List<Interaction> history) {
        return true;
    }
}