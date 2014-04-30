package live.wallpaper.Units;

import android.graphics.Canvas;

public class Bullet extends Unit{

    private float dx;
    private float dy;

    public Bullet(float x, float y, double pX, float pY, int team) {
        super(x, y, team, 0.8f, 6f, Type.Bullet);
        double length=Math.sqrt((pX-x)*(pX-x)+(pY-y)*(pY-y))/getSpeed();
        dx= (float) ((pX-x)/length);
        dy= (float) ((pY-y)/length);
    }

    public void move(Unit[] add) {
        changePosition(dx, dy);

        if (getX()+dx-getWHalf()<0) changeHealth(-10f);
        if (getX()+dx-getWHalf()>getScreenWidth()) changeHealth(-10f);
        if (getY()+dy<0) changeHealth(-10f);
        if (getY()+dy>getScreenHeight()) changeHealth(-10f);
    }

    public void draw(Canvas c) {
        drawBase(c);
    }
}