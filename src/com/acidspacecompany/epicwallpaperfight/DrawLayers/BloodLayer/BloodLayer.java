package com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.Synchroniser;

import java.util.LinkedList;
import java.util.Random;

public class BloodLayer{

    private static LinkedList<Blood> dust;//blood coordinates
    private static Synchroniser synchroniser;
    private static int deltaPos;
    private static int deltaPos2;
    private static Random rnd=new Random();

    public static void init(float coef) {
        dust=new LinkedList<>();
        synchroniser =new Synchroniser("BloodLayerSync");
        deltaPos= (int) (50*coef);
        deltaPos2=deltaPos/2;
    }

    public static void add(float x, float y, float val, int type) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
        synchroniser.waitForUnlockAndLock();
        dust.add(new Blood(x+rnd.nextInt(deltaPos)-deltaPos2, y+rnd.nextInt(deltaPos)-deltaPos2, val, type));
        synchroniser.unlock();
        }
    }

    public static void update(float dt) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
            synchroniser.waitForUnlockAndLock();
            for (int i = 0; i < dust.size(); i++) {
                dust.get(i).update(dt);
                if (dust.get(i).getUseless())
                    dust.remove(i);
            }
            synchroniser.unlock();
        }
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
            synchroniser.waitForUnlockAndLock();
        for (Blood f: dust) {
            f.draw();
        }
            synchroniser.unlock();
        }
    }
}
