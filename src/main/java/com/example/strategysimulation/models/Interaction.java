package com.example.strategysimulation.models;

public class Interaction {
    private boolean decisionA;
    private boolean decisionB;
    private int payoffA;
    private int payoffB;

    public Interaction(boolean decisionA, boolean decisionB, int payoffA, int payoffB) {
        this.decisionA = decisionA;
        this.decisionB = decisionB;
        this.payoffA = payoffA;
        this.payoffB = payoffB;
    }

    public boolean getDecisionA() {
        return decisionA;
    }

    public boolean getDecisionB() {
        return decisionB;
    }

    public int getPayoffA() {
        return payoffA;
    }

    public int getPayoffB() {
        return payoffB;
    }
}