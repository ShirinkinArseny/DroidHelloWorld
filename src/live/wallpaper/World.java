package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import live.wallpaper.DrawLayers.*;
import live.wallpaper.Units.*;

import java.util.*;

public class World {

    private SurfaceHolder surfaceHolder;

    private Random rnd = new Random();
    private int width, height;//size of screen
    private boolean active = true;//is working
    //private Ticker gcTime;//timer for hand calling for gc()
    private long lastTime;

    public World(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;

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

        UnitLayer.init();
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
                        float delta = (cTime - lastTime)/1000f;
                        lastTime = cTime;
                        update(delta);
                        Canvas c = surfaceHolder.lockCanvas();
                        draw(c);
                        surfaceHolder.unlockCanvasAndPost(c);
                        spawnTimer.tick(delta);
                        if (spawnTimer.getIsNextRound())
                            autoSpawn();
                        //gcTime.tick(delta);
                        //if (gcTime.getIsNextRound()) {
                        //    System.gc();
                        //}
                    } catch (Exception e) {
                        //EXCEPTION, LOL
                    }
                }
            }

        }, 0, 10);
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (this) {
            Unit.setScreenSize(width, height);
            UnitLayer.resize(width, height);
            this.width = width;
            this.height = height;
            this.notify();
            TerritoryLayer.resize(width, height);
        }
    }

    public boolean doTouchEvent(MotionEvent event) {
        float posX = event.getX();
        float posY = event.getY();
        int team = (posX < width / 2) ? 0 : 1;

        boolean giant = 0 == rnd.nextInt(100);
        if (giant) {
            UnitLayer.spawn(new Giant(posX, posY, team));
            showMessage(posX, posY, "GIANT SPAWNED!", team);
        } else
            UnitLayer.spawn(new Man(posX, posY, team));
        return true;
    }

    private void autoSpawn() {
        for (int team = 0; team < 2; team++) {
            float x = ((team == 0) ? 0 : width * 2 / 3) + rnd.nextInt(width / 3);
            float y = rnd.nextInt(height);
            boolean gigant = 0 == rnd.nextInt(100);
            if (gigant) {
                UnitLayer.spawn(new Giant(x, y, team));
                showMessage(x, y, "GIANT SPAWNED!", team);
                return;
            }
            boolean tower = 0 == rnd.nextInt(80);
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
        UnitLayer.update(dt);
        SpawnsLayer.update(dt);
        BloodLayer.update(dt);
        MessagesLayer.update(dt);
    }

    private void draw(Canvas canvas) {
        TerritoryLayer.draw(canvas);
        BloodLayer.draw(canvas);
        SpawnsLayer.draw(canvas);
        UnitLayer.draw(canvas);
        MessagesLayer.draw(canvas);
    }
}
        