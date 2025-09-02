package com.example.strategysimulation;

import com.example.strategysimulation.strategies.Strategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
import java.util.Map;

public class Main extends Application {
    private Simulation simulation;
    private LineChart<Number, Number> lineChart;

    private Slider playersSlider;
    private Slider roundsSlider;
    private Slider interactionsSlider;

    private Button startButton;
    private Button resetButton;

    private HBox legendBox;

    // Strategy name to color mapping
    private final Map<String, String> strategyColors = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Strategy Simulation");

        // Initialize strategy colors
        initializeStrategyColors();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Rounds");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Prevalence");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Strategy Prevalence Over Time");
        lineChart.setLegendVisible(false); // Disable default legend

        BorderPane root = new BorderPane();
        root.setCenter(lineChart);

        VBox controlPane = createControlPane();
        root.setRight(controlPane);

        legendBox = new HBox(10);
        legendBox.setAlignment(Pos.CENTER);
        legendBox.setPadding(new Insets(10));
        root.setBottom(legendBox);

        Scene scene = new Scene(root, 2000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeStrategyColors() {
        // Assign colors to each strategy
        strategyColors.put("AlwaysDefect", "#d62728"); // Red
        strategyColors.put("AlwaysCooperate", "#1f77b4"); // Blue
        strategyColors.put("TitForTat", "#2ca02c"); // Green
        strategyColors.put("GrimTrigger", "#ff7f0e"); // Orange
        strategyColors.put("RandomStrategy", "#7f7f7f"); // Gray
        strategyColors.put("Pavlov", "#9467bd"); // Purple
        strategyColors.put("GenerousTitForTat", "#bcbd22"); // Yellow
        strategyColors.put("Prober", "#8c564b"); // Brown
        strategyColors.put("TitForTwoTats", "#17becf"); // Cyan (new color)
        strategyColors.put("TitForTatWithRandomForgiveness", "#e377c2"); // Pink
    }

    private VBox createControlPane() {
        playersSlider = createSlider("Number of Players");
        roundsSlider = createSlider("Number of Rounds");
        interactionsSlider = createSlider("Interactions per Round");

        startButton = new Button("Start");
        startButton.setOnAction(event -> startSimulation());

        resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetSimulation());

        VBox vbox = new VBox(10,
                new Label("Players"), playersSlider,
                new Label("Rounds"), roundsSlider,
                new Label("Interactions"), interactionsSlider,
                startButton, resetButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(200);
        return vbox;
    }

    private Slider createSlider(String label) {
        Slider slider = new Slider(50, 1000, 50);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(4);
        slider.setBlockIncrement(50);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        return slider;
    }

    private void startSimulation() {
        int numPlayers = (int) playersSlider.getValue();
        int numRounds = (int) roundsSlider.getValue();
        int numInteractions = (int) interactionsSlider.getValue();

        // Initialize the simulation with the values from the sliders
        simulation = new Simulation(numRounds, numPlayers, numInteractions);

        // Start the simulation in a separate thread
        new Thread(() -> {
            simulation.runSimulation();
        }).start();

        // Update UI components correctly
        updateUI();
    }


    private void resetSimulation() {
        if (simulation != null) {
            simulation.stopSimulation();
        }
        lineChart.getData().clear();
        legendBox.getChildren().clear();
        playersSlider.setValue(10);
        roundsSlider.setValue(10);
        interactionsSlider.setValue(10);
    }

    private void updateUI() {
        new Thread(() -> {
            while (true) {
                if (!simulation.isSimulationRunning()) {
                    break; // Break out of loop when simulation ends
                }
                try {
                    Thread.sleep(100); // Adjust sleep duration
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Map<Strategy, List<Double>> strategyPrevalence = simulation.getStrategyPrevalence();
                Platform.runLater(() -> {
                    lineChart.getData().clear(); // Clear previous data
                    legendBox.getChildren().clear(); // Clear previous legend

                    for (Map.Entry<Strategy, List<Double>> entry : strategyPrevalence.entrySet()) {
                        XYChart.Series<Number, Number> series = new XYChart.Series<>();
                        String strategyName = entry.getKey().getClass().getSimpleName();
                        series.setName(strategyName);

                        List<Double> prevalenceList = entry.getValue();
                        for (int round = 0; round < prevalenceList.size(); round++) {
                            series.getData().add(new XYChart.Data<>(round, prevalenceList.get(round)));
                        }

                        lineChart.getData().add(series);

                        // Set color for the series after it has been added to the chart
                        Platform.runLater(() -> {
                            String color = strategyColors.getOrDefault(strategyName, "#000000"); // Default color is black
                            series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + color + ";");

                            // Apply color to data points (symbols)
                            for (XYChart.Data<Number, Number> data : series.getData()) {
                                data.getNode().setStyle("-fx-background-color: " + color + ", white;");
                            }
                        });

                        // Add custom legend entry
                        String color = strategyColors.getOrDefault(strategyName, "#000000"); // Default color is black
                        HBox legendEntry = createLegendEntry(strategyName, color);
                        legendBox.getChildren().add(legendEntry);
                    }
                });
            }
        }).start();
    }

    private HBox createLegendEntry(String strategyName, String color) {
        HBox legendEntry = new HBox(10);
        legendEntry.setAlignment(Pos.CENTER);

        Rectangle colorBox = new Rectangle(20, 10, Color.web(color));
        Label nameLabel = new Label(strategyName);
        nameLabel.setStyle("-fx-font-size: 12;");

        legendEntry.getChildren().addAll(colorBox, nameLabel);
        return legendEntry;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
