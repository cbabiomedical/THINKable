package com.example.thinkableproject.sample;

import java.util.ArrayList;

public class Brain_Waves {

    private float alpha;
    private float beta;
    private float gamma;
    private float theta;
    private float delta;
    ArrayList brain_waves;

    public ArrayList<Brain_Waves> getWeather() {
        return brain_waves;
    }

    public Brain_Waves(float alpha, float beta, float gamma, float theta, float delta) {
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.theta = theta;
        this.delta = delta;
    }

    public Brain_Waves() {
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBeta() {
        return beta;
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "Brain_Waves{" +
                "alpha=" + alpha +
                ", beta=" + beta +
                ", gamma=" + gamma +
                ", theta=" + theta +
                ", delta=" + delta +
                '}';
    }
}
