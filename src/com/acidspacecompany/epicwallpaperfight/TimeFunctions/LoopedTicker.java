package com.acidspacecompany.epicwallpaperfight.TimeFunctions;

public class LoopedTicker implements TimeFunction{

    private float length;
    private float time=0;
    private final Runnable onTick;

    public void tick(float delta) {
        if (time>0) {
            time-=delta;
        }
        else {
            time=length;
            onTick.run();
        }
    }

    public float getValue() {
        return time;
    }

    public LoopedTicker(float limit, Runnable onTick) {
        super();
        this.length=limit;
        this.time=0;
        this.onTick=onTick;
    }
}
