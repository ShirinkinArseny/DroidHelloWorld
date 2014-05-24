package live.wallpaper.TimeFunctions;

public class FlappyTimeFunction extends TimeFunction{

    private final float flapValue;
    private int value=0;

    public float getValue() {
        return value;
    }

    public void flap() {
        time=flapValue;
        value++;
        action.run();
    }

    public void tick(float delta) {
        if (time>0)
        time-=delta;
        else if (value!=0) value=0;
    }

    public FlappyTimeFunction(float flap, Runnable onFlap) {
        super(0, onFlap);
        time=0;
        flapValue=flap;
    }
}
