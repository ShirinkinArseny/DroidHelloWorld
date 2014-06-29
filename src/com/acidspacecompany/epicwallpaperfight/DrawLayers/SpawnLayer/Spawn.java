package com.acidspacecompany.epicwallpaperfight.DrawLayers.SpawnLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.Geometry.Rectangle;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LinearTimeFunction;

public class Spawn extends Rectangle{

    private static int w, h;
    private final LinearTimeFunction timing;
    private boolean noNeedMore=false;
    private static int spawnTexture;//spawn texture
    private static float[] scaleMatrix;
    private float[] translateMatrix;

    public static void init(int pic, int w, int h) {
        spawnTexture=pic;
        Spawn.w=w;
        Spawn.h=h;
        scaleMatrix=Graphic.generateScaleMatrix(w, h);
    }

    public boolean getUseless() {
        return noNeedMore;
    }

    public void update(float dt) {
        timing.tick(dt);
    }

    public Spawn(float x, float y) {
        super(x, y, w, h);
        timing = new LinearTimeFunction(LocalConfigs.getFloatValue(LocalConfigs.spawnsShowTime), 1f, 0f,
                new Runnable() {
                    @Override
                    public void run() {
                        noNeedMore = true;
                    }
                }
        );
        translateMatrix=Graphic.generateResultMatrix(getX0(), getY0(), w, h);
    }

    public void draw() {
        Graphic.bindResultMatrix(translateMatrix);
        Graphic.drawBitmap(timing.getValue());
    }

    public void prepareDraw() {
        Graphic.bindScaleMatrix(scaleMatrix);
        Graphic.bindBitmap(spawnTexture);
    }
}
