package com.acidspacecompany.epicwallpaperfight.Units;

public class ControlledUnit extends NotControlledUnit {
    private float dx;
    private float dy;

    protected float getDX() {
        return dx;
    }

    protected float getDY() {
        return dy;
    }

    public ControlledUnit(float x, float y, int w, int h, int team, float health, float speed, Type t) {
        super(x, y, w, h, team, health, speed, t);
    }

    public void setWay(float x, float y) {
        double dl=Math.sqrt((getX()-x)*(getX()-x)+(getY()-y)*(getY()-y));
        dx= (float) ((x-getX())/dl)*getSpeed();
        dy= (float) ((y-getY())/dl)*getSpeed();
    }
}