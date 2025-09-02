package com.example.strategysimulation;

import com.example.strategysimulation.models.Interaction;
import com.example.strategysimulation.models.Player;
import com.example.strategysimulation.strategies.Strategy;
import com.example.strategysimulation.utils.Strategies;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Simulation {
    private int numPlayers;
    private int numRounds;
    private int numInteractionsPerRound;

    private List<Player> players;
    private Map<Strategy, List<Double>> strategyPrevalence;
    private volatile boolean simulationRunning; // Flag to track simulation state
    private ExecutorService executor;

    public Simulation(int numRounds, int numPlayers, int numInteractionsPerRound) {
        this.numRounds = numRounds;
        this.numPlayers = numPlayers;
        this.numInteractionsPerRound = numInteractionsPerRound;
        players = new ArrayList<>();
        strategyPrevalence = new HashMap<>();
        initializePlayers();
    }

    private void initializePlayers() {
        List<Strategy> strategies = Strategies.ALL_STRATEGIES;
        int numStrategies = strategies.size();
        int playersPerStrategy = numPlayers / numStrategies;
        int remainingPlayers = numPlayers % numStrategies;

        List<Player> tempPlayers = new ArrayList<>();

        // Assign strategies equally
        for (Strategy strategy : strategies) {
            for (int i = 0; i < playersPerStrategy; i++) {
                tempPlayers.add(new Player(strategy));
            }
        }

        // Assign the remaining players randomly
        Random random = new Random();
        for (int i = 0; i < remainingPlayers; i++) {
            Strategy randomStrategy = strategies.get(random.nextInt(numStrategies));
            tempPlayers.add(new Player(randomStrategy));
        }

        // Shuffle the list to ensure randomness in initial positions
        Collections.shuffle(tempPlayers);
        players.addAll(tempPlayers);
    }

    public void runSimulation() {
        simulationRunning = true; // Set simulationRunning to true when simulation starts
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int round = 0; round < numRounds; round++) {
            if (!simulationRunning) {
                break; // Exit the loop if the simulation is stopped
            }
            interact();
            calculateFitness();
            reproduce();
            trackStrategyPrevalence(round);
        }
        simulationRunning = false; // Set simulationRunning to false when simulation ends
        executor.shutdown();
    }

    public void stopSimulation() {
        simulationRunning = false; // Set simulationRunning to false, to stop the simulation
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow(); // Shutdown the executor service immediately
        }
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    private void interact() {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < numInteractionsPerRound; i++) {
            tasks.add(() -> {
                int indexA = (int) (Math.random() * numPlayers);
                int indexB;
                do {
                    indexB = (int) (Math.random() * numPlayers);
                } while (indexA == indexB);

                Player playerA = players.get(indexA);
                Player playerB = players.get(indexB);

                boolean decisionA = playerA.getStrategy().decide(playerA.getHistory());
                boolean decisionB = playerB.getStrategy().decide(playerB.getHistory());

                int payoffA = getPayoff(decisionA, decisionB);
                int payoffB = getPayoff(decisionB, decisionA);

                Interaction interaction = new Interaction(decisionA, decisionB, payoffA, payoffB);
                playerA.addInteraction(interaction);
                playerB.addInteraction(interaction);

                return null;
            });
        }
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getPayoff(boolean decisionA, boolean decisionB) {
        if (decisionA && decisionB) return 3;
        if (decisionA) return 0;
        if (decisionB) return 5;
        return 1;
    }

    private void calculateFitness() {
        double totalPayoff = players.stream().mapToDouble(player -> player.getHistory().stream().mapToDouble(Interaction::getPayoffA).sum()).sum();
        for (Player player : players) {
            double payoff = player.getHistory().stream().mapToDouble(Interaction::getPayoffA).sum();
            player.setFitness(payoff / totalPayoff);
        }
    }

    private void reproduce() {
        double totalFitness = players.stream().mapToDouble(Player::getFitness).sum();
        List<Player> newPlayers = new ArrayList<>();
        List<Callable<Player>> tasks = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            tasks.add(() -> {
                double randomValue = Math.random() * totalFitness;
                double cumulativeFitness = 0.0;
                for (Player player : players) {
                    cumulativeFitness += player.getFitness();
                    if (cumulativeFitness >= randomValue) {
                        return new Player(player.getStrategy());
                    }
                }
                return new Player(Strategies.getRandomStrategy()); // Fallback
            });
        }
        try {
            newPlayers.addAll(executor.invokeAll(tasks).stream().map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        players = newPlayers;
    }

    private void trackStrategyPrevalence(int round) {
        for (Strategy strategy : Strategies.ALL_STRATEGIES) {
            long count = players.stream().filter(player -> player.getStrategy().getClass().equals(strategy.getClass())).count();
            strategyPrevalence.computeIfAbsent(strategy, k -> new ArrayList<>()).add((double) count / numPlayers);
        }
    }

    public Map<Strategy, List<Double>> getStrategyPrevalence() {
        return strategyPrevalence;
    }
}
