package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.Geometry.Point;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private Random rnd = new Random();
    private boolean active = true;//is working
    private long lastTime;
    private SurfaceHolder holder;

    private static final float pictureSizeCoef=0.5f;
    private static Bitmap getScaledResource(Resources res, int id, int size) {
        return
        Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(res, id), (int)(size*pictureSizeCoef), (int)(size*pictureSizeCoef), true);
    }

    public World(Context context) {

        Bitmap[][] menTexture = new Bitmap[2][4];
        Resources res = context.getResources();
        menTexture[0][0] = getScaledResource(res, R.drawable.red, 32);
        menTexture[0][1] = getScaledResource(res, R.drawable.bigred, 114);
        menTexture[0][2] = getScaledResource(res, R.drawable.redtower, 32);
        menTexture[1][0] = getScaledResource(res, R.drawable.blue, 32);
        menTexture[1][1] = getScaledResource(res, R.drawable.bigblue, 114);
        menTexture[1][2] = getScaledResource(res, R.drawable.bluetower, 32);
        menTexture[0][3] = getScaledResource(res, R.drawable.bullet, 32);
        menTexture[1][3] = menTexture[0][3];
        Unit.init(menTexture);

        //gcTime = new Ticker(5);

        TimerLayer.init();
        UnitLayer.init();
        WindLayer.init(getScaledResource(context.getResources(), R.drawable.noise, 512));
        TerritoryLayer.init(BitmapFactory.decodeResource(context.getResources(), R.drawable.background));
        BloodLayer.init(new Bitmap[]{getScaledResource(context.getResources(), R.drawable.blood, 64),
                getScaledResource(context.getResources(), R.drawable.coal, 64)});
        MessagesLayer.init();
        SpawnsLayer.init(getScaledResource(context.getResources(), R.drawable.spawn, 74));
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
        final Ticker spawnTimer = new Ticker(0.2f);
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
                            if (spawnTimer.getIsNextRound())
                                autoSpawn();
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
            Configs.displayHeight = height;
            Configs.displayWidth = width;
            TerritoryLayer.resize(width, height);
        }
    }

    private live.wallpaper.Geometry.Point getSpawnPoint(int team) {
        if (Configs.displayWidth > Configs.displayHeight) {
            return new Point(

                    ((team == 0) ?
                            Configs.worldHorizontalBorders :
                            Configs.displayWidth * 2 / 3 - Configs.worldHorizontalBorders)
                    + rnd.nextInt(Configs.displayWidth / 3),

                    rnd.nextInt(Configs.displayHeight - Configs.worldVerticalBottomBorders
                            - Configs.worldVerticalTopBorders)
                            + Configs.worldVerticalTopBorders
            );
        } else {
            return new Point(

                    rnd.nextInt(Configs.displayWidth - 2 * Configs.worldHorizontalBorders)
                            + Configs.worldHorizontalBorders,

                    ((team == 0) ?
                            Configs.worldVerticalTopBorders :
                            Configs.displayHeight * 2 / 3 - Configs.worldVerticalBottomBorders)
                            + rnd.nextInt(Configs.displayWidth / 3)
            );
        }

    }

    private void autoSpawn() {
        for (int team = 0; team < 2; team++) {
            Point p = getSpawnPoint(team);
            boolean gigant = 0 == rnd.nextInt(Configs.worldGianSpawnProbability);
            if (gigant) {
                UnitLayer.spawn(new Giant(p, team));
                showMessage(p, "GIANT SPAWNED!", team);
                return;
            }
            boolean tower = 0 == rnd.nextInt(Configs.worldTowerSpawnProbability);
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
        