package live.wallpaper.DrawLayers;

import android.graphics.Color;
import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.LoopedTicker;

public class TimerLayer {

    private static LoopedTicker round;
    private static int pBig;
    private static int pSmallRed, pSmallBlue;
    private static int height2;
    private static int height2_3;
    private static int width4;
    private static int width2;
    private static float bigSize, smallSize;

    public static void init() {
    }

    public static void reInit(int w, int h) {
        round=new LoopedTicker(Configs.getIntValue(Configs.timerTimer), new Runnable() {
            @Override
            public void run() {
                UnitLayer.killEverybody();
            }
        });
        height2=h/2;
        height2_3=h*2/3;
        width4 =w/4;
        width2=w/2;

        pBig=Color.rgb(Configs.getGrayFontColor()[0], Configs.getGrayFontColor()[1],
                Configs.getGrayFontColor()[2]);

        pSmallRed=Color.rgb(Configs.getRedFontColor()[0], Configs.getRedFontColor()[1],
                Configs.getRedFontColor()[2]);

        pSmallBlue=Color.rgb(Configs.getBlueFontColor()[0], Configs.getBlueFontColor()[1],
                Configs.getBlueFontColor()[2]);
    }

    public static void resize(int w, int h) {
        height2=h/2;
        height2_3=h*2/3;
        width4 =w/4;
        width2=w/2;
        bigSize=w/7;
        smallSize=w/20;
    }

    public static void update(float dt) {
        round.tick(dt);
    }

    private static String getTime() {
        int ms= 10*Configs.getIntValue(Configs.timerTimer)-(int)(10*round.getValue());
        ms%=10;
        int seconds= Configs.getIntValue(Configs.timerTimer)-(int)(round.getValue());
        int minutes=seconds/60;
        seconds%=60;
        return minutes+":"+(seconds>9?seconds:"0"+seconds)+":"+ms;
    }

    public static void draw() {
        if (Configs.getBooleanValue(Configs.timerDraw)) {
            Graphic.drawText(width4, height2, bigSize, pBig, getTime());
            int[] kills = UnitLayer.getTeamSizes();
            Graphic.drawText(width2 - 100, height2_3, smallSize, pSmallRed, String.valueOf(kills[1]));
            Graphic.drawText(width2, height2_3, smallSize, pSmallBlue, String.valueOf(kills[0]));
        }
    }
}
