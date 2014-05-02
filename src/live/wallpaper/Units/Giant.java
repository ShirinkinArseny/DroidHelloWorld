package live.wallpaper.Units;

public class Giant extends Unit{

    private static final float speed=110f;

    public Giant(float x, float y, int team) {
        super(x, y, team, 0.1f, speed, Type.Giant);
    }
}