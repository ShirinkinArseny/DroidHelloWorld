package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.TimeFunctions.LoopedTicker;

public class TimerLayer {

    private static LoopedTicker round;
    private static Paint pBig;
    private static Paint pSmallRed, pSmallBlue;
    private static int height2;
    private static int height2_3;
    private static int width4;
    private static int width2;

    public static void init() {
        pBig=new Paint();
        pBig.setAntiAlias(true);
        pBig.setTextSize(300f);
        pSmallRed=new Paint();
        pSmallRed.setAntiAlias(true);
        pSmallRed.setTextSize(20f);
        pSmallBlue=new Paint();
        pSmallBlue.setAntiAlias(true);
        pSmallBlue.setTextSize(20f);
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

        pBig.setTextSize(w/4);
        pBig.setColor(Color.rgb(Configs.getGrayFontColor()[0], Configs.getGrayFontColor()[1],
                Configs.getGrayFontColor()[2]));
        pBig.setAlpha(Configs.getGrayFontColor()[3]);

        pSmallRed.setTextSize(w/20);
        pSmallBlue.setTextSize(w/20);
        pSmallRed.setColor(Color.rgb(Configs.getRedFontColor()[0], Configs.getRedFontColor()[1],
                Configs.getRedFontColor()[2]));
        pSmallRed.setAlpha(Configs.getRedFontColor()[3]);
        pSmallBlue.setColor(Color.rgb(Configs.getBlueFontColor()[0], Configs.getBlueFontColor()[1],
                Configs.getBlueFontColor()[2]));
        pSmallBlue.setAlpha(Configs.getBlueFontColor()[3]);
    }

    public static void resize(int w, int h) {
        height2=h/2;
        height2_3=h*2/3;
        width4 =w/4;
        width2=w/2;
        pBig.setTextSize(w/7);
        pSmallRed.setTextSize(w/20);
        pSmallBlue.setTextSize(w/20);
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

    public static void draw(Canvas canvas) {
        if (Configs.getBooleanValue(Configs.timerDraw)) {
            canvas.drawText(getTime(), width4, height2, pBig);
            int[] kills = UnitLayer.getTeamSizes();
            canvas.drawText(String.valueOf(kills[1]), width2 - 100, height2_3, pSmallRed);
            canvas.drawText(String.valueOf(kills[0]), width2, height2_3, pSmallBlue);
        }
    }
}
