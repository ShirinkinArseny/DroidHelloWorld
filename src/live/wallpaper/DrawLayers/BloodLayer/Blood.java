package live.wallpaper.DrawLayers.BloodLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.OneTimeTicker;

public class Blood {

    private final float x;
    private final float y;
    private final int type;
    private final OneTimeTicker timing;
    private boolean noNeedMore=false;
    private static int dustTexture[];//blood texture

    public static void init(int[] pics) {
        dustTexture=pics;
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

    public Blood(float x, float y, float startValue, int type) {
        this.x=x;
        this.y=y;
        this.type=type;
        timing=new OneTimeTicker(Configs.getFloatValue(Configs.bloodVisibleTime)+startValue, new Runnable() {
            @Override
            public void run() {
                noNeedMore=true;
            }
        });
    }

    public void draw() {
        Graphic.drawBitmap(dustTexture[type], x, y);
    }
}
