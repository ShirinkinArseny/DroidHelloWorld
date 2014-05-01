package live.wallpaper.Units;

import live.wallpaper.Ticker;

public class Tower extends Unit{

    private float aimX, aimY;
    private Ticker shot;

    public Tower(float x, float y, int team) {
        super(x, y, team, 10f, 3f, Type.Tower);
        setWay(getScreenWidth()/2, getScreenHeight()/2);
        shot=new Ticker(20);
        shot.getIsNextRound();
        shot.tick();
    }

    public float getPower() {
        return 20f;
    }

    public Unit dropTheBomb() {
        return new Bullet(getX(), getY(), aimX, aimY, getTeam());
    }

    public void move(Unit[] add) {
        shot.tick();
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