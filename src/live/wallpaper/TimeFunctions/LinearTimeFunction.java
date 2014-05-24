package live.wallpaper.TimeFunctions;

public class LinearTimeFunction extends TimeFunction{

    private final float delta;
    private final float from;

    public float getValue() {
        return from+delta*time/length;
    }

    public LinearTimeFunction(float length, float from, float to, Runnable onStop) {
        super(length, onStop);
        this.from=from;
        delta=to-from;
    }
}
