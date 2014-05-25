package live.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import live.wallpaper.AI.SimpleAI;
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

        loadTextures();

        UnitLayer.init();
        BloodLayer.init();
        MessagesLayer.init();
        Bullet.reInit(pictureSizeCoef);
    }

    private static void loadTextures() {
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

        int[][] sizes=new int[4][2];
        for (int i=0; i<4; i++) {
            sizes[i][0] = menTexture[0][i].getWidth();
            sizes[i][1] = menTexture[0][i].getHeight();
        }

        int[][] menTextureIDs=new int[4][4];
        for (int i=0; i<4; i++)
            for (int j=0; j<4;j++)
                menTextureIDs[i][j]=Graphic.genTexture(menTexture[i][j]);

        Unit.init(menTextureIDs, sizes, pictureSizeCoef);

        Bitmap canva=getScaledResource(res, R.drawable.grid, 512);
        CanvaLayer.init(Graphic.genTexture(canva), canva.getWidth(), canva.getHeight());

        Bitmap blood1=getScaledResource(res, R.drawable.blood, 64);
        Bitmap blood2=getScaledResource(res, R.drawable.coal, 64);
        Blood.init(new int[]{Graphic.genTexture(blood1), Graphic.genTexture(blood2)});

        Bitmap spawn=getScaledResource(res, R.drawable.spawn, 74);
        SpawnsLayer.init(Graphic.genTexture(spawn), spawn.getWidth()/2, spawn.getHeight()/2);
    }

    public static void reInit() {
        SimpleAI.reInit();
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

    public void updateAndDraw() {
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

    public void run() {
        lastTime = System.currentTimeMillis();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateAndDraw();
            }
        }, 0, 10);
    }

    public void setSurface(SurfaceHolder s, int width, int height) {
           CanvasEngine.setHolder(s);
           setSurface(width,height);
    }

    public void setSurface(int width, int height) {
        TerritoryLayer.resize(width, height);
        UnitLayer.resize(width, height);
        TimerLayer.resize(width, height);
        CanvaLayer.resize(width, height);
        Configs.setDisplayHeight(height);
        Configs.setDisplayWidth(width);
    }

    private void update(float dt) {
        TimerLayer.update(dt);
        UnitLayer.update(dt);
        SpawnsLayer.update(dt);
        BloodLayer.update(dt);
        MessagesLayer.update(dt);
        CanvaLayer.update(dt);
    }

    private void draw() {
        TerritoryLayer.draw();
        CanvaLayer.draw();
        TimerLayer.draw();
        BloodLayer.draw();
        SpawnsLayer.draw();
        UnitLayer.draw();
        MessagesLayer.draw();
    }
}
        