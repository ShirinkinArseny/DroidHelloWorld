package live.wallpaper.Units;

import android.graphics.Bitmap;
import live.wallpaper.Geometry.Point;
import live.wallpaper.Geometry.Rectangle;

public class NotControlledUnit extends Rectangle {
    private int team;
    protected float health;
    private float healthCoef;
    private float power;
    private static Bitmap[][] menTexture;
    public enum Type {Man, Giant, Tower, Bullet}
    private Type type;
    private float speed;

    public static void init(Bitmap[][] menTextures) {
        menTexture=menTextures;
    }

    protected static Bitmap getBitmap(int team, Type type) {
        switch (type) {
            case Man:    return menTexture[team][0];
            case Giant:  return menTexture[team][1];
            case Tower:  return menTexture[team][2];
            case Bullet: return menTexture[team][3];
        }
        return null;
    }

    protected Bitmap getBitmap() {
        switch (type) {
            case Man:    return menTexture[team][0];
            case Giant:  return menTexture[team][1];
            case Tower:  return menTexture[team][2];
            case Bullet: return menTexture[team][3];
        }
        return null;
    }

    public NotControlledUnit(float x, float y, int team, float healthCoef, float speed, Type type) {
        super(x, y, getBitmap(team, type).getWidth(), getBitmap(team, type).getHeight());
        this.team=team;
        this.health=1f;
        this.speed=speed;
        this.type=type;
        this.healthCoef=healthCoef;
        power=1/healthCoef;
    }

    public NotControlledUnit(Point p, int team, float healthCoef, float speed, Type type) {
        super(p, getBitmap(team, type).getWidth(), getBitmap(team, type).getHeight());
        this.team=team;
        this.health=1f;
        this.speed=speed;
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