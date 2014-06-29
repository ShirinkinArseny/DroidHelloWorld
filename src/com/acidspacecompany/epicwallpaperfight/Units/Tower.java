package com.acidspacecompany.epicwallpaperfight.Units;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.Geometry.Point;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LoopedTicker;

public class Tower extends Unit{

    private float aimX, aimY;
    private final LoopedTicker shot;
    private Unit addBuffer;
    private float[] resultMatrixTexture;
    private float[] resultMatrixShadow;

    public Tower(Point p, int team) {
        super(p, team, 10f, 3f, Type.Tower);
        setWay(LocalConfigs.getDisplayWidth()/2, LocalConfigs.getDisplayHeight()/2);
        shot=new LoopedTicker(1f, new Runnable() {
            @Override
            public void run() {
                if (getSquaredLength(aimX, aimY) < Bullet.getSquareDistance())
                    addBuffer=dropTheBomb();
            }
        });
        resultMatrixTexture =Graphic.generateResultMatrix(getX0(), getY0(), getWidth(), getHeight());
        resultMatrixShadow =Graphic.generateResultMatrix(getShadowX0(), getShadowY0(),
                getShadowWidth(), getShadowHeight());
    }

    public float getPower() {
        return 30f;
    }

    public Unit dropTheBomb() {
        return new Bullet(getX(), getY(), aimX, aimY, getTeam());
    }

    public Unit getAddition() {
        return addBuffer;
    }

    public void clearAddition() {
        addBuffer=null;
    }

    public void move(float dt) {
        shot.tick(dt);
    }

    public void setWay(float x, float y) {
        aimX=x;
        aimY=y;
    }

    public void drawShadow() {
        Graphic.bindResultMatrix(resultMatrixShadow);
        Graphic.drawBitmap();
    }

    public void drawBase() {
        Graphic.bindResultMatrix(resultMatrixTexture);
        Graphic.drawBitmap();
    }

    public void reMatrix() {
        resultMatrixTexture =Graphic.generateResultMatrix(getX0(), getY0(), getWidth(), getHeight());
        resultMatrixShadow =Graphic.generateResultMatrix(getShadowX0(), getShadowY0(),
                getShadowWidth(), getShadowHeight());
    }
}