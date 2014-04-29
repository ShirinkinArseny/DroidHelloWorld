package live.wallpaper.Units;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Geometry.Rectangle;

import java.util.Random;

public class NotControlledUnit extends Rectangle {
    private int team;
    protected float health;
    private float healthCoef;
    private static Bitmap[][] menTexture;
    public enum Type {Man, Giant, Tower}
    private Type type;
    private float speed;
    private static float screenWidth, screenHeight;

    public static void setScreenSize(float w, float h) {
        screenWidth=w;
        screenHeight=h;
    }

    public static void init(Bitmap[][] menTextures) {
        menTexture=menTextures;
    }

    protected static float getScreenWidth() {
        return screenWidth;
    }

    protected static float getScreenHeight() {
        return screenHeight;
    }

    protected static Bitmap getBitmap(int team, Type type) {
        switch (type) {
            case Man:    return menTexture[team][0];
            case Giant:  return menTexture[team][1];
            case Tower:  return menTexture[team][2];
        }
        return null;
    }

    protected Bitmap getBitmap() {
        switch (type) {
            case Man:    return menTexture[team][0];
            case Giant:  return menTexture[team][1];
            case Tower:  return menTexture[team][2];
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
        return healthCoef;
    }
}