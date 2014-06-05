package live.wallpaper.DrawLayers;

import live.wallpaper.Configs.LocalConfigs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.LoopedTicker;

public class TimerLayer {

    private static LoopedTicker round;
    private static float[] pBig;
    private static float[] pSmallRed, pSmallBlue;
    private static int height2;
    private static int height2_3;
    private static int width4;
    private static int width2;
    private static float bigSize, smallSize;

    public static void reInit(int w, int h) {
        round=new LoopedTicker(LocalConfigs.getIntValue(LocalConfigs.timerTimer), new Runnable() {
            @Override
            public void run() {
                UnitLayer.killEverybody();
            }
        });
        height2=h/2;
        height2_3=h*2/3;
        width4 =w/4;
        width2=w/2;

        pBig= LocalConfigs.getGrayFontColor();
        pSmallRed= LocalConfigs.getRedFontColor();
        pSmallBlue= LocalConfigs.getBlueFontColor();
    }

    public static void resize(int w, int h) {
        height2=h/3;
        width4 =w/5;
        width2=w*3/5;
        bigSize=w/7;
        height2_3= (int) (height2+bigSize*1.3f);
        smallSize=w/15;
    }

    public static void update(float dt) {
        round.tick(dt);
    }

    private static String getTime() {
        int ms= 10* LocalConfigs.getIntValue(LocalConfigs.timerTimer)-(int)(10*round.getValue());
        ms%=10;
        int seconds= LocalConfigs.getIntValue(LocalConfigs.timerTimer)-(int)(round.getValue());
        int minutes=seconds/60;
        seconds%=60;
        return minutes+":"+(seconds>9?seconds:"0"+seconds)+":"+ms;
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.timerDraw)) {
            Graphic.drawText(width4, height2, bigSize, pBig[0], pBig[1], pBig[2], pBig[3], getTime());
            int[] kills = UnitLayer.getTeamSizes();
            Graphic.drawText(width2 - smallSize*5, height2_3, smallSize,
                    pSmallRed[0], pSmallRed[1], pSmallRed[2], pSmallRed[3], String.valueOf(kills[1]));
            Graphic.drawText(width2, height2_3, smallSize,
                    pSmallBlue[0], pSmallBlue[1], pSmallBlue[2], pSmallBlue[3], String.valueOf(kills[0]));
        }
    }
}
