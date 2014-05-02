package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

public class SpawnsLayer{

    private static Bitmap spawnTexture;//spawn texture
    private static LinkedList<float[]> spawnsAddBuffer;//spawn coordinates
    private static LinkedList<float[]> spawns;//spawn coordinates
    private static Paint p;

    public static void init(Bitmap b) {
        spawnTexture=b;
        p=new Paint();
        p.setColor(Color.WHITE);
        spawns=new LinkedList<>();
        spawnsAddBuffer=new LinkedList<>();
    }

    public static void addSpawn(float x, float y) {
        spawnsAddBuffer.add(new float[]{x-spawnTexture.getWidth()/2, y-spawnTexture.getHeight()/2, 1f});
    }

    public static void update(float dt) {
        for (int i=0; i<spawns.size(); i++) {
            spawns.get(i)[2]-=1f*dt;
            if (spawns.get(i)[2]<=0)
                spawns.remove(i);
        }

        while (!spawnsAddBuffer.isEmpty()) {
        spawns.add(spawnsAddBuffer.get(0));
        spawnsAddBuffer.remove(0);
        }
    }

    public static void draw(Canvas canvas) {
        for (float[] f: spawns) {
            p.setAlpha((int) (f[2]*256));
            canvas.drawBitmap(spawnTexture, f[0], f[1], p);
        }
    }
}
