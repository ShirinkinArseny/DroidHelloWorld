package live.wallpaper.DrawLayers.BloodLayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;
import live.wallpaper.TimeFunctions.OneTimeTicker;

public class Blood {

    private float x, y;
    private int type;
    private OneTimeTicker timing;
    private boolean noNeedMore=false;
    private static Bitmap dustTexture[];//blood texture
    private static Paint p;

    public static void init(Bitmap[] pics) {
        dustTexture=pics;
        p=new Paint();
        p.setColor(Color.WHITE);
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
        timing=new OneTimeTicker(Configs.getBloodVisibleTime()+startValue, new Runnable() {
            @Override
            public void run() {
                noNeedMore=true;
            }
        });
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(dustTexture[type], x, y, p);
    }

}
