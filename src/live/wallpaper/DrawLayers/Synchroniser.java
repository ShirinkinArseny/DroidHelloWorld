package live.wallpaper.DrawLayers;

import android.util.Log;

public class Synchroniser {

    private boolean lock =false;
    private String name;

    public Synchroniser(String debugInfo) {
        name=debugInfo;
    }

    public void lock() {
        lock =true;
        //Log.i("LOCK", "Locked: "+name);
    }

    public void unlock() {
        lock =false;
        //Log.i("LOCK", "Unlocked: "+name);
    }

    public void waitForUnlock() {
        try {
            while (lock)
                Thread.sleep(5);
        } catch (InterruptedException e) {
            System.exit(0);
        }
    }
}
