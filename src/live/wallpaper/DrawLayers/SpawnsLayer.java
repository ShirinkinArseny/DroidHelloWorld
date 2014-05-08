package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;

import java.util.LinkedList;

public class SpawnsLayer{

    private static Bitmap spawnTexture;//spawn texture
    private static LinkedList<float[]> spawnsAddBuffer;//spawn coordinates
    private static LinkedList<float[]> spawns;//spawn coordinates
    private static Paint p;
    private static Synchroniser syncer;

    public static void init(Bitmap b) {
        spawnTexture=b;
        p=new Paint();
        p.setColor(Color.WHITE);
        spawns=new LinkedList<>();
        spawnsAddBuffer=new LinkedList<>();
        syncer=new Synchroniser();
    }

    public static void addSpawn(float x, float y) {
        if (Configs.spawnsDraw) {
            syncer.waitForUnlock();
            syncer.lock();
            spawnsAddBuffer.add(new float[]{x - spawnTexture.getWidth() / 2, y - spawnTexture.getHeight() / 2, 1f});
            syncer.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.spawnsDraw) {
        for (int i=0; i<spawns.size(); i++) {
            spawns.get(i)[2]-= Configs.spawnsHideCoef *dt;
            if (spawns.get(i)[2]<=0)
                spawns.remove(i);
        }

        while (!spawnsAddBuffer.isEmpty()) {
        spawns.add(spawnsAddBuffer.get(0));
        spawnsAddBuffer.remove(0);
        }       }
    }

    public static void draw(Canvas canvas) {
        if (Configs.spawnsDraw)
        for (float[] f: spawns) {
            p.setAlpha((int) (f[2]*256));
            canvas.drawBitmap(spawnTexture, f[0], f[1], p);
        }
    }
}
