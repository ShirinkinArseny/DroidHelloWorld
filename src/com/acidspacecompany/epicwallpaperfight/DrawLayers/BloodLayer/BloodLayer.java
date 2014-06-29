package com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.Synchroniser;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

import java.util.LinkedList;
import java.util.Random;

import static com.acidspacecompany.epicwallpaperfight.World.getScaledValue;

public class BloodLayer{

    private static LinkedList<Blood>[] dust;//blood coordinates
    private static Synchroniser synchroniser;
    private static int deltaPos;
    private static int deltaPos2;
    private static Random rnd=new Random();

    public static void init() {
        dust=new LinkedList[]{new LinkedList(), new LinkedList()};
        synchroniser =new Synchroniser("BloodLayerSync");
        deltaPos= (int) (getScaledValue(50));
        deltaPos2=deltaPos/2;
    }

    public static void add(float x, float y, float val, int type) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
        synchroniser.waitForUnlockAndLock();
        dust[type].add(new Blood(x+rnd.nextInt(deltaPos)-deltaPos2, y+rnd.nextInt(deltaPos)-deltaPos2, val, type));
        synchroniser.unlock();
        }
    }

    public static void update(float dt) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
            synchroniser.waitForUnlockAndLock();
            for (int type=0; type<2; type++)
            for (int i = 0; i < dust[type].size(); i++) {
                dust[type].get(i).update(dt);
                if (dust[type].get(i).getUseless())
                    dust[type].remove(i);
            }
            synchroniser.unlock();
        }
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.bloodDraw)) {
            synchroniser.waitForUnlockAndLock();
            for (int type=0; type<2; type++) {
                if (!dust[type].isEmpty()) {
                    dust[type].get(0).prepareDraw();
                for (Blood f : dust[type]) {
                    f.draw();
                }
                    Graphic.unBindMatrices();
                }
            }
            synchroniser.unlock();
        }
    }
}
