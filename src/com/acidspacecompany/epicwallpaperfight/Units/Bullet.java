package com.acidspacecompany.epicwallpaperfight.Units;

import static com.acidspacecompany.epicwallpaperfight.World.getScaledValue;

public class Bullet extends Unit{

    private final float dx;
    private final float dy;
    private static final float speed=300f;
    private static final float dHealth=-1f;
    private static float distance=Math.abs(speed/dHealth);
    private static float squareDistance=distance*distance;

    public static float getSquareDistance() {
        return squareDistance;
    }

    public static void init() {
        distance=Math.abs(getScaledValue(speed/dHealth));
        squareDistance=distance*distance;
    }

    public Bullet(float x, float y, double pX, float pY, int team) {
        super(x, y, team, 1.5f, speed, Type.Bullet);
        double length=Math.sqrt((pX-x)*(pX-x)+(pY-y)*(pY-y))/getSpeed();
        dx= (float) ((pX-x)/length);
        dy= (float) ((pY-y)/length);
    }

    public float getPower() {
        return 2f;
    }

    public void move(float dt) {
        changePosition(dx*dt, dy*dt);
        changeHealth(dHealth*dt);
    }

    public void draw() {
        drawBase();
    }
}