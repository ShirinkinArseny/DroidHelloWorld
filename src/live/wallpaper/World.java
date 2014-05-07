package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private Random rnd = new Random();
    private int width, height;//size of screen
    private boolean active = true;//is working
    private long lastTime;
    private SurfaceHolder holder;

    public World(Context context) {

        Bitmap[][] menTexture = new Bitmap[2][4];
        Resources res=context.getResources();
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
                            if (c!=null) {
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
            holder=s;
            Configs.displayHeight=height;
            Configs.displayWidth=width;
            UnitLayer.resize(width, height);
            TimerLayer.resize(width, height);
            WindLayer.resize(width, height);
            this.width = width;
            this.height = height;
            this.notify();
            TerritoryLayer.resize(width, height);
        }
    }

    private void autoSpawn() {
        for (int team = 0; team < 2; team++) {
            float x = ((team == 0) ? Configs.worldHorizontalBorders : width * 2 / 3-Configs.worldHorizontalBorders)
                    + rnd.nextInt(width / 3);
            float y = rnd.nextInt(height-Configs.worldVerticalBottomBorders-Configs.worldVerticalTopBorders)+Configs.worldVerticalTopBorders;
            boolean gigant = 0 == rnd.nextInt(Configs.worldGianSpawnProbability);
            if (gigant) {
                UnitLayer.spawn(new Giant(x, y, team));
                showMessage(x, y, "GIANT SPAWNED!", team);
                return;
            }
            boolean tower = 0 == rnd.nextInt(Configs.worldTowerSpawnProbability);
            if (tower) {
                UnitLayer.spawn(new Tower(x, y, team));
                showMessage(x, y, "TOWER BUILT!", team);
                return;
            }
            UnitLayer.spawn(new Man(x, y, team));
        }
    }

    private void showMessage(float x, float y, String text, int color) {
        MessagesLayer.showMessage(x, y, text, color);
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
        