package live.wallpaper.TimeFunctions;

public abstract class TimeFunction {

    protected float length;
    protected float time;
    protected Runnable action;

    public float getValue() {
        return time;
    }

    public void tick(float delta) {
        time+=delta;
        if (time>=length) {
            action.run();
        }
    }

    public TimeFunction(float length, Runnable onStop) {
        this.length=length;
        time=0;
        action =onStop;
    }
}