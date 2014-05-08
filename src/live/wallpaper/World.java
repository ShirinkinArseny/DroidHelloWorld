package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.Geometry.*;
import live.wallpaper.Geometry.Point;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private Random rnd = new Random();
    private boolean active = true;//is working
    private long lastTime;
    private SurfaceHolder holder;

    public World(Context context) {

        Bitmap[][] menTexture = new Bitmap[2][4];
        Resources res = context.getResources();
        menTexture[0][0] = BitmapFactory.decodeResource(res, R.drawable.red);
        menTexture[0][1] = BitmapFactory.decodeResource(res, R.drawable.bigred);
        menTexture[0][2] = BitmapFactory.decodeResource(res, R.drawable.redtower);
        menTexture[1][0] = BitmapFactory.decodeResource(res, R.drawable.blue);
        menTexture[1][1] = BitmapFactory.decodeResource(res, R.drawable.bigblue);
        menTexture[1][2] = BitmapFactory.decodeResource(res, R.drawable.bluetower);
        menTexture[0][3] = BitmapFactory.decodeResource(res, R.drawable.bullet);
        menTexture[1][3] = menTexture[0][3];
        Unit.init(menTexture);

        //gcTime = new Ticker(5);

        TimerLayer.init();
        UnitLayer.init();
        WindLayer.init(BitmapFactory.decodeResource(context.getResources(), R.drawable.noise));
        TerritoryLayer.init(BitmapFactory.decodeResource(context.getResources(), R.drawable.background));
        BloodLayer.init(new Bitmap[]{BitmapFactory.decodeResource(context.getResources(), R.drawable.blood),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.coal)});
        MessagesLayer.init();
        SpawnsLayer.init(BitmapFactory.decodeResource(context.getResources(), R.drawable.spawn));
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
        