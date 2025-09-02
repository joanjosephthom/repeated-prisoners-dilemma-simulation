package com.example.strategysimulation.strategies;
import com.example.strategysimulation.models.Interaction;
import java.util.List;

public interface Strategy {
    boolean decide(List<Interaction> history);
}