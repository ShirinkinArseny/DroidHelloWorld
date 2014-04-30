package live.wallpaper.Units;

import live.wallpaper.Ticker;

public class Tower extends Unit{

    private float aimX, aimY;
    private Ticker shot;

    public Tower(float x, float y, int team) {
        super(x, y, team, 1f, 3f, Type.Tower);
        setWay(getScreenWidth()/2, getScreenHeight()/2);
        shot=new Ticker(50);
        shot.getIsNextRound();
        shot.tick();
    }

    public Unit dropTheBomb() {
        return new Bullet(getX(), getY(), aimX, aimY, getTeam());
    }

    public void move(Unit[] add) {
        shot.tick();
        if (shot.getIsNextRound()) {
            add[0]=dropTheBomb();
        }
    }

    public void setWay(float x, float y) {
        aimX=x;
        aimY=y;
    }
}