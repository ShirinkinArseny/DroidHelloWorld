package com.acidspacecompany.epicwallpaperfight.TimeFunctions;

public class LinearTimeFunction implements TimeFunction{

    private final float delta;
    private final float from;
    private final float length;
    protected float time;
    private final Runnable onStop;

    public float getValue() {
        return from+delta*time/length;
    }

    public void tick(float delta) {
        time+=delta;
        if (time>=length) {
            onStop.run();
        }
    }

    public LinearTimeFunction(float length, float from, float to, Runnable onStop) {
        super();
        this.onStop=onStop;
        this.length=length;
        this.from=from;
        delta=to-from;
    }
}
