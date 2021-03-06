package com.acidspacecompany.epicwallpaperfight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;
import com.acidspacecompany.epicwallpaperfight.AI.AI;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.*;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer.Blood;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.BloodLayer.BloodLayer;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.MessagesLayer.MessagesLayer;
import com.acidspacecompany.epicwallpaperfight.DrawLayers.SpawnLayer.SpawnsLayer;
import com.acidspacecompany.epicwallpaperfight.Network.NetworkDebugger;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.Units.*;

import java.util.*;

public class World {

    private boolean active = true;//is working
    private static boolean physicIsWorking = true;
    private long lastTime;
    private static Resources res;
    private static float pictureSizeCoef;
    private static NetworkDebugger nwd=new NetworkDebugger();

    public static void setPhysicIsWorking(boolean physicIsWorking) {
        World.physicIsWorking = physicIsWorking;
    }

    public static Bitmap getScaledBitmap(Bitmap b, int size) {
        return Bitmap.createScaledBitmap(b, size, size, true);
    }

    private static Bitmap getBitmapFromResource(int resourceId) {
        return BitmapFactory.decodeResource(res, resourceId);
    }

    private static Bitmap getScaledResource(Resources res, int id, int size) {
        return getScaledBitmap(BitmapFactory.decodeResource(res, id), (int) (size*pictureSizeCoef));
    }

    public World(Context context) {
        res=context.getResources();
        //init();
        LocalConfigs.init(context);
    }

    public static float getScaledValue(float value) {
        return pictureSizeCoef*value;
    }

    public static void init() {
        DisplayMetrics metrics = res.getDisplayMetrics();
        pictureSizeCoef=Math.max(metrics.widthPixels, metrics.heightPixels)/1100f;

        loadTextures();
        UnitLayer.init();
        BloodLayer.init();
        MessagesLayer.init();
        StatisticLayer.init();
        Bullet.init();
        reInit();
    }

    private static int power2nearest(float s) {
        for (int i=1;;i*=2) {
            if (i>=s) {
                return i;
            }
        }
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
        menTexture[1][3] = getScaledResource(res, R.drawable.bullet, 28);
        menTexture[2][0] = getScaledResource(res, R.drawable.shadow, 128);
        menTexture[2][1] = getScaledResource(res, R.drawable.shadow, 384);
        menTexture[2][2] = getScaledResource(res, R.drawable.towershadow, 128);
        menTexture[3][0] = getScaledResource(res, R.drawable.shadow, 128);
        menTexture[3][1] = getScaledResource(res, R.drawable.shadow, 384);
        menTexture[3][2] = getScaledResource(res, R.drawable.towershadow, 128);

        int[][] sizes=new int[4][2];
        for (int i=0; i<4; i++) {
            sizes[i][0] = menTexture[0][i].getWidth();
            sizes[i][1] = menTexture[0][i].getHeight();
        }

        int[][] menTextureIDs=new int[4][4];
        for (int i=0; i<4; i++)
            for (int j=0; j<4;j++)
                if(menTexture[i][j]!=null)
                    menTextureIDs[i][j]=Graphic.genTexture(menTexture[i][j]);

        Unit.init(menTextureIDs, sizes);

        //Bitmap canva=getScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.grid), power2nearest(getScaledValue(256)));
        //CanvaLayer.init((int) (getScaledValue(canva.getWidth())), Graphic.genInfinityTexture(canva), LocalConfigs.getPaintingType());
        CanvaLayer.init((int)getScaledValue(512));

        Bitmap blood1=getScaledResource(res, R.drawable.blood, 80);
        Bitmap blood2=getScaledResource(res, R.drawable.coal, 80);
        Blood.init(new int[]{Graphic.genTexture(blood1), Graphic.genTexture(blood2)}, blood1.getWidth(), blood1.getHeight());

        Bitmap spawn=getScaledResource(res, R.drawable.spawn, 74);
        SpawnsLayer.init(Graphic.genTexture(spawn), spawn.getWidth(), spawn.getHeight());
    }

    public static void reInit() {
        AI.reInit();
        CanvaLayer.reInit();
        MessagesLayer.reInit();
        TerritoryLayer.reInit();
        TimerLayer.reInit();
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

    private static long lastDeltaTime=0;
    private static float lowpassedTPU=0;
    private static long fps=0;
    private static float lowpassedFPS=0;
    private static float delta;
    public void updateAndDraw() {
        if (active) {
            long cTime = System.currentTimeMillis();
            delta = (cTime - lastTime) / 1000f;
            lastTime = cTime;
            if (physicIsWorking)
                update(delta);
            Graphic.startDraw();
            draw();
            lastDeltaTime=System.currentTimeMillis()-cTime;
            lowpassedTPU=0.99f*lowpassedTPU+0.01f*lastDeltaTime;
            fps= (long) (1/delta);
            lowpassedFPS=0.99f*lowpassedFPS+0.01f*fps;
        }
    }

    public static long getFPS() {
        return fps;
    }

    public static long getLowPassedFPS() {
        return (long) lowpassedFPS;
    }

    public static long getLowPassedTPU() {
        return (long) lowpassedTPU;
    }

    public static long getTPU() {
        return lastDeltaTime;
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

    public static void resize(int width, int height) {
        if (LocalConfigs.getDisplayWidth()!=width && LocalConfigs.getDisplayHeight()!=height) {
            Graphic.resize(width, height);
            UnitLayer.resize(width, height);
            TimerLayer.resize(width, height);
            TimerLayer.resize(width, height);
            LocalConfigs.resize(width, height);
            TerritoryLayer.resize(width, height);
        }
    }

    private void update(float dt) {
        TimerLayer.update(dt);
        UnitLayer.update(dt);
        SpawnsLayer.update(dt);
        BloodLayer.update(dt);
        MessagesLayer.update(dt);
        CanvaLayer.update(dt);
    }

    public static void setBackgroung(Bitmap backgroung, Graphic.PaintingType paintingType, int width, float opacity)
    {
        CanvaLayer.setDrawingBitmap(backgroung, paintingType, width, opacity);
    }
    public static void setBackgroung(int resourceId, Graphic.PaintingType paintingType, int width, float opacity)
    {
        setBackgroung(getBitmapFromResource(resourceId), paintingType,width, opacity);
    }

    private void draw() {
        Graphic.begin(Graphic.Mode.DRAW_RECTANGLES);
        TerritoryLayer.draw();
        Graphic.begin(Graphic.Mode.FILL_BITMAP);
        CanvaLayer.draw();
        Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
        SpawnsLayer.draw();
        BloodLayer.draw();
        TimerLayer.draw();
        MessagesLayer.draw();
        UnitLayer.draw();
        Graphic.begin(Graphic.Mode.DRAW_RECTANGLES);
        UnitLayer.drawRectangles();
        //Graphic.begin(Graphic.Mode.DRAW_BITMAPS);
        //StatisticLayer.draw();
    }
}
        