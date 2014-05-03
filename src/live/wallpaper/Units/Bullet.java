package live.wallpaper.Units;

import android.graphics.Canvas;

public class Bullet extends Unit{

    private float dx;
    private float dy;
    private static final float speed=200f;
    private static final float dHealth=-1f;
    private static final float distance=Math.abs(speed/dHealth);
    public static final float squareDistance=distance*distance;

    public Bullet(float x, float y, double pX, float pY, int team) {
        super(x, y, team, 1.2f, speed, Type.Bullet);
        double length=Math.sqrt((pX-x)*(pX-x)+(pY-y)*(pY-y))/getSpeed();
        dx= (float) ((pX-x)/length);
        dy= (float) ((pY-y)/length);
    }

    public float getPower() {
        return 2f;
    }

    public void move(Unit[] add, float dt) {
        changePosition(dx*dt, dy*dt);

        changeHealth(dHealth*dt);

        if (getX()+dx- getHalfWidth()<0) changeHealth(-10f);
        if (getX()+dx- getHalfWidth()>getScreenWidth()) changeHealth(-10f);
        if (getY()+dy<0) changeHealth(-10f);
        if (getY()+dy>getScreenHeight()) changeHealth(-10f);
    }

    public void draw(Canvas c) {
        drawBase(c);
    }
}