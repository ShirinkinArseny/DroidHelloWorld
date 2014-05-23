package live.wallpaper.DrawLayers.BloodLayer;

import android.graphics.Canvas;
import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.Synchroniser;

import java.util.LinkedList;

public class BloodLayer{

    private static LinkedList<Blood> dust;//blood coordinates
    private static Synchroniser syncer;

    public static void init() {
        dust=new LinkedList<>();
        syncer=new Synchroniser("BloodLayerSync");
    }

    public static void add(float x, float y, float val, int type) {
        if (Configs.getBooleanValue(Configs.bloodDraw)) {
        syncer.waitForUnlockAndLock();
        dust.add(new Blood(x, y, val, type));
        syncer.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.getBooleanValue(Configs.bloodDraw)) {
            syncer.waitForUnlockAndLock();
            for (int i = 0; i < dust.size(); i++) {
                dust.get(i).update(dt);
                if (dust.get(i).getUseless())
                    dust.remove(i);
            }
            syncer.unlock();
        }
    }

    public static void draw() {
        if (Configs.getBooleanValue(Configs.bloodDraw)) {
            syncer.waitForUnlockAndLock();
        for (Blood f: dust) {
            f.draw();
        }
            syncer.unlock();
        }
    }
}
