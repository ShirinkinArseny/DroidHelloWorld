package live.wallpaper.Units;

import live.wallpaper.Geometry.Point;

public class Giant extends Unit{

    private static final float speed=110f;

    public Giant(Point p, int team) {
        super(p, team, 0.1f, speed, Type.Giant);
    }
}