package live.wallpaper.DrawLayers.BloodLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.Geometry.Rectangle;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.OneTimeTicker;

public class Blood extends Rectangle {

    private final int type;
    private static int w, h;
    private final OneTimeTicker timing;
    private boolean noNeedMore = false;
    private static int dustTexture[];//blood texture

    public static void init(int[] pics, int w, int h) {
        dustTexture = pics;
        Blood.w = w;
        Blood.h = h;
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
        timing = new OneTimeTicker(Configs.getFloatValue(Configs.bloodVisibleTime) + startValue, new Runnable() {
            @Override
            public void run() {
                noNeedMore = true;
            }
        });
    }

    public void draw() {
        Graphic.drawBitmap(dustTexture[type], this);
    }

}
