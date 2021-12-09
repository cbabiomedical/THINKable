package com.example;

public class EEG_Values {
    private int alpha;
    private int beta;
    private int gamma;
    private  int theta;
    private int delta;

    public EEG_Values() {
    }

    public EEG_Values(int alpha, int beta, int gamma, int theta, int delta) {
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.theta = theta;
        this.delta = delta;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public int getGamma() {
        return gamma;
    }

    public void setGamma(int gamma) {
        this.gamma = gamma;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}
