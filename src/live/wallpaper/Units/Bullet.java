package live.wallpaper.Units;

import android.graphics.Canvas;

public class Bullet extends Unit{

    private float dx;
    private float dy;
    private static float speed=400f;
    private static float dHealth=-1f;
    private static float distance=Math.abs(speed/dHealth);
    private static float squareDistance=distance*distance;
    //todo: учесть спидкоеф

    public static float getSquareDistance() {
        return squareDistance;
    }

    public static void reInit(float speedCoef) {
        distance=Math.abs(speed*speedCoef/dHealth);
        squareDistance=distance*distance;
    }

    public Bullet(float x, float y, double pX, float pY, int team) {
        super(x, y, team, 1.2f, speed, Type.Bullet);
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

    public void draw(Canvas c) {
        drawBase(c);
    }
}