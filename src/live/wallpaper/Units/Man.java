package live.wallpaper.Units;

public class Man extends Unit{
    private static final float speed=80f;

    public Man(float x, float y, int team) {
        super(x, y, team, 1f, speed, Type.Man);
    }
}