package com.acidspacecompany.epicwallpaperfight.TimeFunctions;

public class LoopedTicker extends TimeFunction{

    public void tick(float delta) {
        super.tick(delta);
        if (time>=length) {
            time-=length;
        }
    }

    public LoopedTicker(float limit, Runnable onTick) {
        super(limit, onTick);
    }
}
