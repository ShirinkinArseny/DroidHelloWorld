package live.wallpaper.Units;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.Geometry.Point;
import live.wallpaper.TimeFunctions.FlappyTimeFunction;

public class Unit extends ControlledUnit {

    private static Paint p;
    private FlappyTimeFunction kills;

    public static void init(Bitmap[][] menTexture, float pictureSizeCoef) {
        NotControlledUnit.init(menTexture, pictureSizeCoef);
        p = new Paint();
    }

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, team, health, speed, t);
        kills=new FlappyTimeFunction(2f, new Runnable() {
            @Override
            public void run() {
                if (kills.getValue()>5)
                    MessagesLayer.showMessage(getX(), getY(), (int)kills.getValue()+"-kill!", getTeam());
            }
        });
    }

    public Unit(Point p2, int team, float health, float speed, Type t) {
        this(p2.getX(), p2.getY(), team, health, speed, t);
    }

    public void bumpKills() {
        kills.flap();
    }

    public void changeHealth(float h) {
        health += getHealthCoef() * h;
        if (health > 1f) health = 1f;
    }

    public void move(float dt) {
        float dx = getDX() * dt;
        float dy = getDY() * dt;
        changePosition(dx, dy);
        if (getX().isNaN() || getY().isNaN()) {
            Log.i("Unit.move", getX() + " " + getY());
            changeHealth(-10f);
        }
        kills.tick(dt);
    }

    protected void drawBase(Canvas c) {
        c.drawBitmap(getBitmap(), getX() - getHalfWidth(), getY() - getHalfHeight(), null);
    }

    protected void drawHealth(Canvas c) {
        float health=Math.max(0, this.getHealth());
        p.setColor(Color.rgb((int) (255 * (1 - health)), (int) (192 * health), 0));
        c.drawRect(getX() - getHalfWidth(), getY() - 2 - getHalfHeight(),
                getX() + getWidth() * health - getHalfWidth(), getY() - getHalfHeight(), p);
    }

    public void draw(Canvas c) {
        drawBase(c);
        drawHealth(c);
    }
}