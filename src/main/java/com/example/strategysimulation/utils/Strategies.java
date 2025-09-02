package com.example.strategysimulation.utils;

import com.example.strategysimulation.strategies.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Strategies {
    public static final List<Strategy> ALL_STRATEGIES = Arrays.asList(
            new AlwaysCooperate(),
            new AlwaysDefect(),
            new TitForTat(),
            new GrimTrigger(),
            new RandomStrategy(),
            new Pavlov(),
            new TitForTwoTats(),
            new GenerousTitForTat(),
            new Prober(),
            new TitForTatWithRandomForgiveness()
    );

    public static Strategy getRandomStrategy() {
        Random random = new Random();
        return ALL_STRATEGIES.get(random.nextInt(ALL_STRATEGIES.size()));
    }
}