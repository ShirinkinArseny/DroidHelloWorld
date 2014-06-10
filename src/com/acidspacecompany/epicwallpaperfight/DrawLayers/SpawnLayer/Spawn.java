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

    public static void init(int pic, int w, int h) {
        spawnTexture=pic;
        Spawn.w=w;
        Spawn.h=h;
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
    }

    public void draw() {
        Graphic.drawBitmap(spawnTexture, this, timing.getValue());
    }

}
