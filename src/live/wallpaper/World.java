package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.DrawLayers.BloodLayer.Blood;
import live.wallpaper.DrawLayers.BloodLayer.BloodLayer;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.DrawLayers.SpawnLayer.SpawnsLayer;
import live.wallpaper.OpenGLIntegration.CanvasEngine;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private boolean active = true;//is working
    private long lastTime;
    private SurfaceHolder holder;
    private static Resources res;
    private static float pictureSizeCoef=1f;

    private static Bitmap getScaledResource(Resources res, int id, int size) {
        return
        Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, id), (int)(size*pictureSizeCoef), (int)(size*pictureSizeCoef), true);
    }

    public World(Context context) {
        res=context.getResources();
        init();
        Configs.init(context);
        reInit();
    }

    private static void init() {
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef=Math.max(metrics.widthPixels, metrics.heightPixels)/1100f;

        Bitmap[][] menTexture = new Bitmap[4][4];
        menTexture[0][0] = getScaledResource(res, R.drawable.red, 32);
        menTexture[0][1] = getScaledResource(res, R.drawable.red, 96);
        menTexture[0][2] = getScaledResource(res, R.drawable.redtower, 32);
        menTexture[0][3] = getScaledResource(res, R.drawable.bullet, 28);
        menTexture[1][0] = getScaledResource(res, R.drawable.blue, 32);
        menTexture[1][1] = getScaledResource(res, R.drawable.blue, 96);
        menTexture[1][2] = getScaledResource(res, R.drawable.bluetower, 32);
        menTexture[1][3] = menTexture[0][3];
        menTexture[2][0] = getScaledResource(res, R.drawable.shadow, 64);
        menTexture[2][1] = getScaledResource(res, R.drawable.shadow, 192);
        menTexture[2][2] = getScaledResource(res, R.drawable.towershadow, 64);
        menTexture[3][0] = getScaledResource(res, R.drawable.shadow, 64);
        menTexture[3][1] = getScaledResource(res, R.drawable.shadow, 192);
        menTexture[3][2] = getScaledResource(res, R.drawable.towershadow, 64);
        Unit.init(menTexture, pictureSizeCoef);

        WindLayer.init(getScaledResource(res, R.drawable.grid, 512));
        Blood.init(new Bitmap[]{getScaledResource(res, R.drawable.blood, 64),
                getScaledResource(res, R.drawable.coal, 64)});
        SpawnsLayer.init(getScaledResource(res, R.drawable.spawn, 74));
        TerritoryLayer.init(BitmapFactory.decodeResource(res, R.drawable.background));

        TimerLayer.init();
        UnitLayer.init();
        BloodLayer.init();
        MessagesLayer.init();
        Bullet.reInit(pictureSizeCoef);
    }

    public static void reInit() {
        MessagesLayer.reInit();
        TerritoryLayer.reInit();
        DisplayMetrics metrics = res.getDisplayMetrics();
        TimerLayer.reInit(metrics.widthPixels, metrics.heightPixels);
    }

    public void pausePainting() {
        active = false;
    }

    public void resumePainting() {
        active = true;
        lastTime = System.currentTimeMillis();
    }

    public void stopPainting() {
        active = false;
    }

    public void run() {
        lastTime = System.currentTimeMillis();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (active) {
                        long cTime = System.currentTimeMillis();
                        float delta = (cTime - lastTime) / 1000f;
                        lastTime = cTime;
                        update(delta);
                        Graphic.startDraw();
                        draw();
                        Graphic.finishDraw();
                }
            }
        }, 0, 10);
    }

    public void setSurface(SurfaceHolder s, int width, int height) {
            CanvasEngine.setHolder(s);
            holder = s;
            UnitLayer.resize(width, height);
            TimerLayer.resize(width, height);
            WindLayer.resize(width, height);
            Configs.setDisplayHeight(height);
            Configs.setDisplayWidth(width);
            TerritoryLayer.resize(width, height);
    }

    private void update(float dt) {
        TerritoryLayer.update(dt);
        TimerLayer.update(dt);
        UnitLayer.update(dt);
        SpawnsLayer.update(dt);
        BloodLayer.update(dt);
        MessagesLayer.update(dt);
        WindLayer.update(dt);
    }

    private void draw() {
        TerritoryLayer.draw();
        WindLayer.draw();
        TimerLayer.draw();
        BloodLayer.draw();
        SpawnsLayer.draw();
        UnitLayer.draw();
        MessagesLayer.draw();
    }
}
        