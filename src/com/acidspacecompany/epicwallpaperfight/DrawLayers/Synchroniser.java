package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import android.util.Log;

public class Synchroniser {

    private boolean lock =false;
    private final String name; //for debug

    public Synchroniser(String name) {
        this.name=name;
    }

    public synchronized void unlock() {
        lock =false;
    }

    public synchronized void waitForUnlockAndLock() {
        try {
            while (lock)
                Thread.sleep(10);
        } catch (InterruptedException e) {
            System.exit(0);
        }
        lock =true;
    }
}
