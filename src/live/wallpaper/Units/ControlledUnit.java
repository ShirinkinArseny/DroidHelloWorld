package live.wallpaper.Units;

public class ControlledUnit extends NotControlledUnit {
    private float xWay;
    private float yWay;

    public ControlledUnit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, team, health, speed, t);
        xWay=x+10;
        yWay=y+10;
    }

    public float getXWay() {
        return xWay;
    }

    public float getYWay() {
        return yWay;
    }

    public void setWay(float x, float y) {
        xWay=x;
        yWay=y;
    }
}