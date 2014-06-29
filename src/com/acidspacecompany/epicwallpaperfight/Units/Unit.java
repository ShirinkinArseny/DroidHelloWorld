package com.acidspacecompany.epicwallpaperfight.Units;

import com.acidspacecompany.epicwallpaperfight.Geometry.Point;
import com.acidspacecompany.epicwallpaperfight.Geometry.Rectangle;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

public class Unit extends ControlledUnit {

    //private FlappyTimeFunction kills;
    private Rectangle shadow;
    private static int[][] menTexture;
    private static int[][] textureSizes;
    private static int[][] shadowSizes;
    private static float[][] texturesScaleMatrices;
    private static float[][] shadowScaleMatrices;
    private float widthHealth;

    protected float getShadowX0() {
        return shadow.getX0();
    }

    protected float getShadowY0() {
        return shadow.getY0();
    }

    protected float getShadowWidth() {
        return shadow.getWidth();
    }

    protected float getShadowHeight() {
        return shadow.getHeight();
    }

    private int getShadowBitmap() {
        return menTexture[getTeam() + 2][getTypeNumber()];
    }

    private int getTextureBitmap() {
        return menTexture[getTeam()][getTypeNumber()];
    }

    private float[] getTextureScaleMatrix() {
        return texturesScaleMatrices[getTypeNumber()];
    }

    private float[] getShadowScaleMatrix() {
        return shadowScaleMatrices[getTypeNumber()];
    }

    public static void init(int[][] menTexture, int[][] sizes) {
        Unit.menTexture = menTexture;
        textureSizes = sizes;
        shadowSizes = new int[4][2];
        for (int i = 0; i < 4; i++) {
            shadowSizes[i][0] = sizes[i][0] * 4;
            shadowSizes[i][1] = sizes[i][1] * 4;
        }
        texturesScaleMatrices = new float[4][];
        shadowScaleMatrices = new float[4][];
        for (int i = 0; i < 4; i++) {
            texturesScaleMatrices[i] = Graphic.generateScaleMatrix(textureSizes[i][0], textureSizes[i][1]);
            shadowScaleMatrices[i] = Graphic.generateScaleMatrix(shadowSizes[i][0], shadowSizes[i][1]);
        }
    }

    public Unit(float x, float y, int team, float health, float speed, Type t) {
        super(x, y, textureSizes[getTypeNumber(t)][0], textureSizes[getTypeNumber(t)][1], team, health, speed, t);
        shadow = new Rectangle(x, y, shadowSizes[getTypeNumber(t)][0], shadowSizes[getTypeNumber(t)][1]);
        //kills=new FlappyTimeFunction(2f, new Runnable() {
        //    @Override
        //    public void run() {
        //        if (kills.getValue()>5)
        //            MessagesLayer.showMessage(getX(), getY(), (int)kills.getValue()+"-KILL!", getTeam());
        //    }
        //});
        changeHealth(0);
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
        widthHealth=getWidth() * health;
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

    public void prepareDrawBase() {
        Graphic.bindBitmap(getTextureBitmap());
        Graphic.bindScaleMatrix(getTextureScaleMatrix());
    }

    public void prepareDrawShadow() {
            Graphic.bindBitmap(getShadowBitmap());
            Graphic.bindScaleMatrix(getShadowScaleMatrix());
    }

    public void drawShadow() {
            Graphic.drawBitmap(shadow.getX0(), shadow.getY0());
    }

    public void drawBase() {
        Graphic.drawBitmap(getX0(), getY0());
    }

    public void drawHealth() {
            float health = Math.max(0, this.health);
            Graphic.drawRect(getX0(), getY0() - 5,
                    getX0() + widthHealth, getY0() - 3,
                    1 - health, 0.75f * health, 0, 1);
    }

    public void kill() {
        health = -1;
    }

    public void reMatrix() {
    }
}