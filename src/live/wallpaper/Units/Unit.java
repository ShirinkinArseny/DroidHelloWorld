package live.wallpaper.Units;

import android.graphics.*;
import android.util.Log;

public class Unit extends ControlledUnit {

    private Paint p;

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, team, health, speed, t);
        p = new Paint();
    }

    public void changeHealth(float h) {
        health += getHealthCoef() * h;
        if (health > 1f) health = 1f;
    }

    public void move(Unit[] add, float dt) {
        float dx = getDX() * dt;
        float dy = getDY() * dt;
        if (getX() + dx - getHalfWidth() < 0) dx = 1;
        if (getX() + dx - getHalfWidth() > getScreenWidth()) dx = -1;
        if (getY() + dy < 0) dy = 1;
        if (getY() + dy > getScreenHeight()) dy = -1;
        changePosition(dx, dy);
        if (getX().isNaN() || getY().isNaN()) {
            Log.i("Unit.move", getX() + " " + getY());
            changeHealth(-10f);
        }
    }

    protected void drawBase(Canvas c) {
        c.drawBitmap(getBitmap(), getX() - getHalfWidth(), getY() - getHalfHeight(), null);
    }

    protected void drawHealth(Canvas c) {
        p.setColor(Color.rgb((int) (255 * (1 - health)), (int) (128 * health), 0));
        c.drawRect(getX() - getHalfWidth(), getY() - 2 - getHalfHeight(),
                getX() + getWidth() * health - getHalfWidth(), getY() - getHalfHeight(), p);
    }

    public void draw(Canvas c) {
        drawBase(c);
        drawHealth(c);
    }
}