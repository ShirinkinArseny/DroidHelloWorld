package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;

import java.util.LinkedList;

public class BloodLayer{

    private static LinkedList<float[]> dust;//blood coordinates
    private static Bitmap dustTexture[];//blood texture
    private static Paint p;
    private static Synchroniser syncer;

    public static void init(Bitmap[] pics) {
        dust=new LinkedList<>();
        dustTexture=pics;
        p=new Paint();
        p.setColor(Color.WHITE);
        syncer=new Synchroniser();
    }

    public static void add(float x, float y, float val, float type) {
        if (Configs.bloodDraw) {
        syncer.waitForUnlock();
        syncer.lock();
            dust.add(new float[]{x, y, val, type});
        syncer.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.bloodDraw) {
            syncer.waitForUnlock();
            syncer.lock();
            for (int i = 0; i < dust.size(); i++) {
                dust.get(i)[2] -= Configs.bloodHideCoef * dt;
                if (dust.get(i)[2] <= 0)
                    dust.remove(i);
            }
            syncer.unlock();
        }
    }

    public static void draw(Canvas canvas) {
        if (Configs.bloodDraw) {
            syncer.waitForUnlock();
            syncer.lock();
        for (float[] f: dust) {
            canvas.drawBitmap(dustTexture[((int) f[3])], f[0], f[1], p);
        }
            syncer.unlock();
        }
    }
}
