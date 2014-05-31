package live.wallpaper.DrawLayers.SpawnLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.Synchroniser;

import java.util.LinkedList;

public class SpawnsLayer{

    private static LinkedList<Spawn> spawns;//spawn coordinates
    private static Synchroniser synchroniser;

    public static void init(int b, int w, int h) {
        Spawn.init(b, w, h);
        spawns=new LinkedList<>();
        synchroniser =new Synchroniser("SpawnsLayerSync");
    }

    public static void addSpawn(float x, float y) {
        if (Configs.getBooleanValue(Configs.spawnsDraw)) {
            synchroniser.waitForUnlockAndLock();
            spawns.add(new Spawn(x, y));
            synchroniser.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.getBooleanValue(Configs.spawnsDraw)) {
            synchroniser.waitForUnlockAndLock();
        for (int i=0; i<spawns.size(); i++) {
            spawns.get(i).update(dt);
            if (spawns.get(i).getUseless())
                spawns.remove(i);
        }
            synchroniser.unlock();
        }
    }

    public static void draw() {
        if (Configs.getBooleanValue(Configs.spawnsDraw)) {
            synchroniser.waitForUnlockAndLock();
            for (Spawn f : spawns) {
                f.draw();
            }
            synchroniser.unlock();
        }
    }
}
