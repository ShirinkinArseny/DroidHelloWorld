package live.wallpaper.Units;

import live.wallpaper.Geometry.Rectangle;

public class NotControlledUnit extends Rectangle {
    private int team;
    protected float health;
    private float healthCoef;
    private float power;
    public enum Type {Man, Giant, Tower, Bullet}
    private Type type;
    private float speed;
    private static float speedCoef;

    public static void init(float speedCoef) {
        NotControlledUnit.speedCoef=speedCoef;
    }

    public static int getTypeNumber(Type type) {
        switch (type) {
            case Man: return 0;
            case Giant:return 1;
            case Tower:return 2;
            case Bullet:return 3;
        }
        return -1;
    }

    public int getTypeNumber() {
        switch (type) {
            case Man: return 0;
            case Giant:return 1;
            case Tower:return 2;
            case Bullet:return 3;
        }
        return -1;
    }

    public NotControlledUnit(float x, float y, float w, float h, int team, float healthCoef, float speed, Type type) {
        super(x, y, w, h);
        this.team=team;
        this.health=1f;
        this.speed=speed*speedCoef;
        this.type=type;
        this.healthCoef=healthCoef;
        power=1/healthCoef;
    }

    public Type getType() {
        return type;
    }

    public int getTeam() {
        return team;
    }

    public float getHealth() {
         return health;
    }

    public float getHealthCoef(){
        return healthCoef;
    }

    public float getSpeed() {
        return speed;
    }

    public float getPower() {
        return power;
    }
}