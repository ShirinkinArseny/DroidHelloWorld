package com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.Geometry.Rectangle;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LinearTimeFunction;

public class Blood extends Rectangle {

    private final int type;
    private static int w, h;
    private LinearTimeFunction timing;
    private boolean noNeedMore = false;
    private static int dustTexture[];//blood texture
    private static float[] scaleMatrix;
    private float[] resultMatrix;

    public static void init(int[] pics, int w, int h) {
        dustTexture = pics;
        Blood.w = w;
        Blood.h = h;
        scaleMatrix=Graphic.generateScaleMatrix(w, h);
    }

    public boolean getUseless() {
        return noNeedMore;
    }

    public void update(float dt) {
        timing.tick(dt);
    }

    public Blood(float x, float y, float startValue, int type) {
        super(x, y, w, h);
        this.type = type;
        timing = new LinearTimeFunction(LocalConfigs.getFloatValue(LocalConfigs.bloodVisibleTime) + startValue, 1f, 0f, new Runnable() {
            @Override
            public void run() {
                noNeedMore = true;
            }
        });
        resultMatrix =Graphic.generateResultMatrix(getX0(), getY0(), w, h);
    }

    public void draw() {
        Graphic.bindResultMatrix(resultMatrix);
        Graphic.drawBitmap(timing.getValue());
    }

    public void prepareDraw() {
        Graphic.bindScaleMatrix(scaleMatrix);
        Graphic.bindBitmap(dustTexture[type]);
    }
}
