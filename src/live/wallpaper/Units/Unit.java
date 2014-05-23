package live.wallpaper.Units;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.Geometry.Point;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.FlappyTimeFunction;

public class Unit extends ControlledUnit {

    private FlappyTimeFunction kills;
    private static Bitmap[][] menTexture;
    private static Point[] sizes;

    private Bitmap getShadow() {
        return menTexture[getTeam()+2][getTypeNumber()];
    }

    private Bitmap getBitmap() {
        return menTexture[getTeam()][getTypeNumber()];
    }

    public static void init(Bitmap[][] menTexture, float pictureSizeCoef) {
        NotControlledUnit.init(pictureSizeCoef);
        Unit.menTexture=menTexture;
        sizes=new Point[menTexture.length];
        for (int i=0; i<4; i++)
            sizes[i]=new Point(menTexture[0][i].getWidth(), menTexture[0][i].getHeight());
    }

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, sizes[getTypeNumber(t)].getX(), sizes[getTypeNumber(t)].getY(), team, health, speed, t);
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
        float dxt = getDX() * dt;
        float dyt = getDY() * dt;
        changePosition(dxt, dyt);
        if (Float.valueOf(getX()).isNaN() || Float.valueOf(getY()).isNaN()) {
            Log.i("Unit.move", getX() + " " + getY());
            changeHealth(-10f);
        }
        kills.tick(dt);
    }

    public void drawShadow() {
        Graphic.drawBitmap(getShadow(), getX() - getWidth(), getY() - getHeight());
    }

    protected void drawBase() {
        Graphic.drawBitmap(getBitmap(), getX() - getHalfWidth(), getY() - getHalfHeight());
    }

    protected void drawHealth() {
        float health=Math.max(0, this.health);
        Graphic.drawRect(getX() - getHalfWidth(), getY() - 2 - getHalfHeight(),
                getX() + getWidth() * health - getHalfWidth(), getY() - getHalfHeight(),
                Color.rgb((int) (255 * (1 - health)), (int) (192 * health), 0), 255);
    }

    public void draw() {
        drawBase();
        drawHealth();
    }
}