package com.acidspacecompany.epicwallpaperfight.Units;

import com.acidspacecompany.epicwallpaperfight.Geometry.Point;
import com.acidspacecompany.epicwallpaperfight.Geometry.Rectangle;
import com.acidspacecompany.epicwallpaperfight.OpenGLIntegration.Graphic;

public class Unit extends ControlledUnit {

    //private FlappyTimeFunction kills;
    private Rectangle shadow;
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
        shadow=new Rectangle(x, y, sizes[getTypeNumber(t)][0]*4, sizes[getTypeNumber(t)][1]*4);
        //kills=new FlappyTimeFunction(2f, new Runnable() {
        //    @Override
        //    public void run() {
        //        if (kills.getValue()>5)
        //            MessagesLayer.showMessage(getX(), getY(), (int)kills.getValue()+"-KILL!", getTeam());
        //    }
        //});
    }

    public Unit(Point p2, int team, float health, float speed, Type t) {
        this(p2.getX(), p2.getY(), team, health, speed, t);
    }

    //public void bumpKills() {
    //    kills.flap();
    //}

    public void changeHealth(float h) {
        health += getHealthCoef() * h;
        if (health > 1f) health = 1f;
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        shadow.setPosition(x, y);
    }

    public void move(float dt) {
        float dxt = getDX() * dt;
        float dyt = getDY() * dt;
        changePosition(dxt, dyt);
        shadow.changePosition(dxt, dyt);
        //kills.tick(dt);
    }

    public void drawShadow() {
        Graphic.drawBitmap(getShadow(), shadow);
    }

    public void drawBase() {
        Graphic.drawBitmap(getBitmap(), this);
    }

    public void drawHealth() {
        float health=Math.max(0, this.health);
        float w2=getWidth() * health/2;
        Graphic.drawRect(getX()-w2, getY0()-5,
                getX()+w2 * health, getY0()-3,
                1 - health, 0.75f *health, 0, 1);
    }

    public void kill() {
        health=-1;
    }
}