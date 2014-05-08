package live.wallpaper.Units;

import live.wallpaper.Configs;
import live.wallpaper.Geometry.Point;
import live.wallpaper.Ticker;

public class Tower extends Unit{

    private float aimX, aimY;
    private Ticker shot;

    public Tower(Point p, int team) {
        super(p, team, 10f, 3f, Type.Tower);
        setWay(Configs.displayWidth/2, Configs.displayHeight/2);
        shot=new Ticker(0.3f);
        shot.getIsNextRound();
    }

    public float getPower() {
        return 30f;
    }

    public Unit dropTheBomb() {
        return new Bullet(getX(), getY(), aimX, aimY, getTeam());
    }

    public void move(Unit[] add, float dt) {
        shot.tick(dt);
        if (shot.getIsNextRound()) {
            if (getSquaredLength(aimX, aimY) < Bullet.squareDistance)
                add[0]=dropTheBomb();
        }
    }

    public void setWay(float x, float y) {
        aimX=x;
        aimY=y;
    }
}