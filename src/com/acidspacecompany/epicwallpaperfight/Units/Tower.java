package com.acidspacecompany.epicwallpaperfight.Units;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.Geometry.Point;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LoopedTicker;

public class Tower extends Unit{

    private float aimX, aimY;
    private final LoopedTicker shot;
    private Unit addBuffer;
    private int resultMatrixTexture;
    private int resultMatrixShadow;

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
        resultMatrixTexture =Graphic.getResultMatrixID(getX0(), getY0(), getWidth(), getHeight());
        resultMatrixShadow =Graphic.getResultMatrixID(getShadowX0(), getShadowY0(),
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
        if (health>0) {
        Graphic.bindResultMatrix(resultMatrixShadow);
        Graphic.drawBitmap();
        }
    }

    public void drawBase() {
        if (health>0) {
            Graphic.bindResultMatrix(resultMatrixTexture);
            Graphic.drawBitmap();
        }
    }


    public void prepareDrawBase() {
        Graphic.bindBitmap(getTextureBitmap());
    }

    public void prepareDrawShadow() {
        Graphic.bindBitmap(getShadowBitmap());
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        resultMatrixTexture =Graphic.getResultMatrixID(getX0(), getY0(), getWidth(), getHeight());
        resultMatrixShadow =Graphic.getResultMatrixID(getShadowX0(), getShadowY0(),
                getShadowWidth(), getShadowHeight());
    }

    public void kill() {
        super.kill();
            Graphic.cleanResultMatrixID(resultMatrixTexture);
            Graphic.cleanResultMatrixID(resultMatrixShadow);
    }
}