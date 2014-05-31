package live.wallpaper.DrawLayers.SpawnLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.LinearTimeFunction;

public class Spawn {

    private final float x;
    private final float y;
    private static float w2, h2;
    private final LinearTimeFunction timing;
    private boolean noNeedMore=false;
    private static int spawnTexture;//spawn texture

    public static void init(int pic, float w2, float h2) {
        spawnTexture=pic;
        Spawn.w2=w2;
        Spawn.h2=h2;
    }

    public boolean getUseless() {
        return noNeedMore;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void update(float dt) {
        timing.tick(dt);
    }

    public Spawn(float x, float y) {
        this.x = x-w2;
        this.y = y-h2;
        timing = new LinearTimeFunction(Configs.getFloatValue(Configs.spawnsShowTime), 1f, 0f,
                new Runnable() {
                    @Override
                    public void run() {
                        noNeedMore = true;
                    }
                }
        );
    }

    public void draw() {
        Graphic.drawBitmap(spawnTexture, x, y, timing.getValue());
    }

}
