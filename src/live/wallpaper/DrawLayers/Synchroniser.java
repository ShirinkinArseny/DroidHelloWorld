package live.wallpaper.DrawLayers;

public class Synchroniser {

    private boolean lock =false;

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
