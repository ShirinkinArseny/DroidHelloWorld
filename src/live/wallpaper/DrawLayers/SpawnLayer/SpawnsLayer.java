package live.wallpaper.DrawLayers.SpawnLayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.Synchroniser;

import java.util.LinkedList;

public class SpawnsLayer{

    private static LinkedList<Spawn> spawns;//spawn coordinates
    private static Synchroniser syncer;

    public static void init(Bitmap b) {
        Spawn.init(b);
        spawns=new LinkedList<>();
        syncer=new Synchroniser();
    }

    public static void reInit() {
    }

    public static void addSpawn(float x, float y) {
        if (Configs.getBooleanValue(Configs.spawnsDraw)) {
            syncer.waitForUnlock();
            syncer.lock();
            spawns.add(new Spawn(x, y));
            syncer.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.getBooleanValue(Configs.spawnsDraw)) {
            syncer.waitForUnlock();
            syncer.lock();
        for (int i=0; i<spawns.size(); i++) {
            spawns.get(i).update(dt);
            if (spawns.get(i).getUseless())
                spawns.remove(i);
        }
            syncer.unlock();
        }
    }

    public static void draw(Canvas canvas) {
        if (Configs.getBooleanValue(Configs.spawnsDraw))
        for (Spawn f: spawns) {
            f.draw(canvas);
        }
    }
}
