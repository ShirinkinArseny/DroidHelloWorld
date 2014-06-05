package live.wallpaper.Units;

import live.wallpaper.Configs.LocalConfigs;
import live.wallpaper.Geometry.Point;
import live.wallpaper.TimeFunctions.LoopedTicker;

public class Tower extends Unit{

    private float aimX, aimY;
    private final LoopedTicker shot;
    private Unit addBuffer;

    public Tower(Point p, int team) {
        super(p, team, 10f, 3f, Type.Tower);
        setWay(LocalConfigs.getDisplayWidth()/2, LocalConfigs.getDisplayHeight()/2);
        shot=new LoopedTicker(1f, new Runnable() {
            @Override
            public void run() {
                if (getSquaredLength(aimX, aimY) < Bullet.getSquareDistance())
                    addBuffer=dropTheBomb();
            }
        });
    }

    public float getPower() {
        return 30f;
    }

    public Unit dropTheBomb() {
        return new Bullet(getX(), getY(), aimX, aimY, getTeam());
    }

    public Unit getAddition() {
        return addBuffer;
    }

    public void clearAddition() {
        addBuffer=null;
    }

    public void move(float dt) {
        shot.tick(dt);
    }

    public void setWay(float x, float y) {
        aimX=x;
        aimY=y;
    }
}