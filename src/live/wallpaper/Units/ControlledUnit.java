package live.wallpaper.Units;

import live.wallpaper.Geometry.Point;

public class ControlledUnit extends NotControlledUnit {
    private float dx;
    private float dy;

    protected float getDX() {
        return dx;
    }

    protected float getDY() {
        return dy;
    }

    public ControlledUnit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, team, health, speed, t);
    }

    public ControlledUnit(Point p, int team, float health, float speed, Type t) {
        super(p, team, health, speed, t);
    }

    public void setWay(float x, float y) {
        double dl=Math.sqrt((getX()-x)*(getX()-x)+(getY()-y)*(getY()-y));
        dx= (float) ((x-getX())/dl)*getSpeed();
        dy= (float) ((y-getY())/dl)*getSpeed();
    }
}