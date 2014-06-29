package com.acidspacecompany.epicwallpaperfight.DrawLayers.SpawnLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.Synchroniser;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

import java.util.LinkedList;

public class SpawnsLayer{

    private static LinkedList<Spawn> spawns;//spawn coordinates
    private static Synchroniser synchroniser;

    public static void init(int b, int w2, int h2) {
        Spawn.init(b, w2, h2);
        spawns=new LinkedList<>();
        synchroniser =new Synchroniser("SpawnsLayerSync");
    }

    public static void addSpawn(float x, float y) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.spawnsDraw)) {
            synchroniser.waitForUnlockAndLock();
            spawns.add(new Spawn(x, y));
            synchroniser.unlock();
        }
    }

    public static void update(float dt) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.spawnsDraw)) {
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
        if (LocalConfigs.getBooleanValue(LocalConfigs.spawnsDraw)) {
            synchroniser.waitForUnlockAndLock();
            if (!spawns.isEmpty()) {
                spawns.get(0).prepareDraw();
                for (Spawn f : spawns) {
                    f.draw();
                }
                Graphic.unBindMatrices();
            }
            synchroniser.unlock();
        }
    }
}
