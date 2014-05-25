package live.wallpaper.Units;

import android.util.Log;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.Geometry.Point;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.FlappyTimeFunction;

public class Unit extends ControlledUnit {

    private FlappyTimeFunction kills;
    private static int[][] menTexture;
    private static int[][] sizes;

    private int getShadow() {
        return menTexture[getTeam()+2][getTypeNumber()];
    }

    private int getBitmap() {
        return menTexture[getTeam()][getTypeNumber()];
    }

    public static void init(int[][] menTexture, int[][] sizes, float pictureSizeCoef) {
        NotControlledUnit.init(pictureSizeCoef);
        Unit.menTexture=menTexture;
        Unit.sizes=sizes;
    }

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, sizes[getTypeNumber(t)][0], sizes[getTypeNumber(t)][1], team, health, speed, t);
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
        Graphic.drawBitmap(getBitmap(), getX0(), getY0());
    }

    public void drawHealth() {
        float health=Math.max(0, this.health);
        Graphic.drawRect(getX0(), getY0()-2,
                getX0() + getWidth() * health, getY0(),
                1 - health, 0.75f *health, 0, 1);
    }

    public void draw() {
        drawBase();
       // drawHealth();
    }
}