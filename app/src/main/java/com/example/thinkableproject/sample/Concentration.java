package com.example.thinkableproject.sample;

import java.util.ArrayList;

public class Concentration {
    private float alpha;
    private float beta;
    private float gamma;
    private float theta;
    private float delta;
    private ArrayList<weatherinfo> weather;

    public Concentration() {
    }

    public Concentration(float delta, float theta, float alpha, float beta, float gamma) {
        this.delta = delta;
        this.theta = theta;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;


    }

    public ArrayList<weatherinfo> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<weatherinfo> weather) {
        this.weather = weather;
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
        return "Concentration{" +
                "alpha=" + alpha +
                ", beta=" + beta +
                ", gamma=" + gamma +
                ", theta=" + theta +
                ", delta=" + delta +
                '}';
    }

    public static class weatherinfo{
        float delta,theta,alpha,beta,gamma;

        public float getDelta() {
            return delta;
        }

        public void setDelta(float delta) {
            this.delta = delta;
        }

        public float getTheta() {
            return theta;
        }

        public void setTheta(float theta) {
            this.theta = theta;
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
    }
}
