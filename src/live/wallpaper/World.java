package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.DrawLayers.BloodLayer.Blood;
import live.wallpaper.DrawLayers.BloodLayer.BloodLayer;
import live.wallpaper.DrawLayers.MessagesLayer.MessagesLayer;
import live.wallpaper.DrawLayers.SpawnLayer.SpawnsLayer;
import live.wallpaper.Geometry.Point;
import live.wallpaper.TimeFunctions.LoopedTicker;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private Random rnd = new Random();
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
        res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef=Math.max(metrics.widthPixels, metrics.heightPixels)/1400f;
        initTextures();
        Configs.init(context);
        reInit();
    }

    public static void reInit() {
        TimerLayer.init();
        UnitLayer.init();
        BloodLayer.init();
        MessagesLayer.init();
        Unit.reInit();
        WindLayer.reInit();
        Blood.reInit();
        SpawnsLayer.reInit();
        TerritoryLayer.reInit();
    }

    private static void initTextures() {
        Bitmap[][] menTexture = new Bitmap[2][4];
        menTexture[0][0] = getScaledResource(res, R.drawable.red, 32);
        menTexture[0][1] = getScaledResource(res, R.drawable.red, 96);
        menTexture[0][2] = getScaledResource(res, R.drawable.redtower, 32);
        menTexture[0][3] = getScaledResource(res, R.drawable.bullet, 32);
        menTexture[1][0] = getScaledResource(res, R.drawable.blue, 32);
        menTexture[1][1] = getScaledResource(res, R.drawable.blue, 96);
        menTexture[1][2] = getScaledResource(res, R.drawable.bluetower, 32);
        menTexture[1][3] = menTexture[0][3];
        Unit.init(menTexture, pictureSizeCoef);
        WindLayer.init(getScaledResource(res, R.drawable.noise, 512));
        Blood.init(new Bitmap[]{getScaledResource(res, R.drawable.blood, 64),
                getScaledResource(res, R.drawable.coal, 64)});
        SpawnsLayer.init(getScaledResource(res, R.drawable.spawn, 74));

        TerritoryLayer.init(BitmapFactory.decodeResource(res, R.drawable.background));

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
        final LoopedTicker spawnTimer = new LoopedTicker(0.2f, new Runnable() {
            @Override
            public void run() {
                autoSpawn();
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (active) {
                    try {
                        long cTime = System.currentTimeMillis();
                        float delta = (cTime - lastTime) / 1000f;
                        lastTime = cTime;
                        update(delta);
                        Canvas c = holder.lockCanvas();
                        if (c != null) {
                            draw(c);
                            holder.unlockCanvasAndPost(c);
                            spawnTimer.tick(delta);
                        }
                    } catch (IllegalArgumentException e) {
                        //Log.i("run", e.getMessage());
                    }
                }
            }

        }, 0, 10);
    }

    public void setSurface(SurfaceHolder s, int width, int height) {
        synchronized (this) {
            holder = s;
            UnitLayer.resize(width, height);
            TimerLayer.resize(width, height);
            WindLayer.resize(width, height);
            Configs.setDisplayHeight(height);
            Configs.setDisplayWidth(width);
            TerritoryLayer.resize(width, height);
        }
    }

    private live.wallpaper.Geometry.Point getSpawnPoint(int team) {
        if (Configs.getDisplayWidth() > Configs.getDisplayHeight()) {
            return new Point(

                    ((team == 0) ?
                            Configs.getIntValue(Configs.worldHorizontalBorders) :
                            Configs.getDisplayWidth() * 2 / 3 - Configs.getIntValue(Configs.worldHorizontalBorders))
                    + rnd.nextInt(Configs.getDisplayWidth() / 3),

                    rnd.nextInt(Configs.getDisplayHeight() - Configs.getIntValue(Configs.worldVerticalBottomBorders)
                            - Configs.getIntValue(Configs.worldHorizontalBorders))
                            +  Configs.getIntValue(Configs.worldVerticalTopBorders)
            );
        } else {
            return new Point(

                    rnd.nextInt(Configs.getDisplayWidth()
                            - 2 * Configs.getIntValue(Configs.worldHorizontalBorders))
                            + Configs.getIntValue(Configs.worldHorizontalBorders),

                    ((team == 0) ?
                            Configs.getIntValue(Configs.worldVerticalTopBorders) :
                            Configs.getDisplayHeight() * 2 / 3 - Configs.getIntValue(Configs.worldVerticalBottomBorders))
                            + rnd.nextInt(Configs.getDisplayWidth() / 3)
            );
        }

    }

    private void autoSpawn() {
        for (int team = 0; team < 2; team++) {
            Point p = getSpawnPoint(team);
            boolean gigant = 0 == rnd.nextInt(Configs.getIntValue(Configs.worldGianSpawnProbability));
            if (gigant) {
                UnitLayer.spawn(new Giant(p, team));
                showMessage(p, "GIANT SPAWNED!", team);
                return;
            }
            boolean tower = 0 == rnd.nextInt(Configs.getIntValue(Configs.worldTowerSpawnProbability));
            if (tower) {
                UnitLayer.spawn(new Tower(p, team));
                showMessage(p, "TOWER BUILT!", team);
                return;
            }
            UnitLayer.spawn(new Man(p, team));
        }
    }

    private void showMessage(Point p, String text, int color) {
        MessagesLayer.showMessage(p, text, color);
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

    private void draw(Canvas canvas) {
        TerritoryLayer.draw(canvas);
        TimerLayer.draw(canvas);
        BloodLayer.draw(canvas);
        SpawnsLayer.draw(canvas);
        UnitLayer.draw(canvas);
        MessagesLayer.draw(canvas);
        WindLayer.draw(canvas);
    }
}
        