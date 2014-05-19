package live.wallpaper.DrawLayers.SpawnLayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.TimeFunctions.LinearTimeFunction;

public class Spawn {

    private float x, y;
    private LinearTimeFunction timing;
    private boolean noNeedMore=false;
    private static Bitmap spawnTexture;//spawn texture
    private static Paint p;

    public static void init(Bitmap pic) {
        spawnTexture=pic;
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

    public Spawn(float x, float y) {
        this.x = x-spawnTexture.getWidth()/2;
        this.y = y-spawnTexture.getHeight()/2;
        timing = new LinearTimeFunction(Configs.getFloatValue(Configs.spawnsShowTime), 255f, 0f,
                new Runnable() {
                    @Override
                    public void run() {
                        noNeedMore = true;
                    }
                }
        );
    }

    public void draw(Canvas canvas) {
        p.setAlpha((int) (timing.getValue()));
        canvas.drawBitmap(spawnTexture, x, y, p);
    }

}
