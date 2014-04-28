package live.wallpaper.Units;

import android.graphics.*;
import android.util.Log;
import live.wallpaper.Geometry.Rectangle;
import live.wallpaper.R;

import java.util.Random;

public class Unit extends ControlledUnit {

    private Paint p;
    private Random rnd;

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, team, health, speed, t);
        p = new Paint();
        rnd=new Random();
    }

    public void changeHealth(float h) {
         health+=getHealthCoef()*h;
         if (health>1f) health=1f;
    }

    public void move() {
        double dl=Math.sqrt((getX()-getXWay())*(getX()-getXWay())+(getY()-getYWay())*(getY()-getYWay()));
        float dx= (float) ((getXWay()-getX())/dl);
        float dy= (float) ((getYWay()-getY())/dl);
        dx*=getSpeed()+getSpeed()*rnd.nextFloat();
        dy*=getSpeed()+getSpeed()*rnd.nextFloat();
        changePosition(dx, dy);
    }

    protected void drawBase(Canvas c) {
        c.drawBitmap(getBitmap(), getX()-getWHalf(), getY()-getHHalf(), null);
    }

    protected void drawHealth(Canvas c) {
        p.setColor(Color.rgb((int)(255*(1-health)), (int)(255*health), 0));
        p.setAlpha(128);
        c.drawRect(getX()-getWHalf(), getY()-2-getHHalf(),
                getX()+getW()*health-getWHalf(), getY()-getHHalf(), p);
    }

    public void draw(Canvas c) {
        drawBase(c);
        drawHealth(c);
    }
}