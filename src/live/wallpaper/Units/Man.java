package live.wallpaper.Units;

import live.wallpaper.Geometry.Point;

public class Man extends Unit{
    private static final float speed=80f;

    public Man(Point p, int team) {
        super(p, team, 1f, speed, Type.Man);
    }
}