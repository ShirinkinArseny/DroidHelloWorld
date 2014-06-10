package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LoopedTicker;

import static com.acidspacecompany.epicwallpaperfight.DrawLayers.UnitLayer.getMiddleXUnits;
import static com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Font.getStringWidth;

public class TimerLayer {

    private static LoopedTicker round;
    private static float[] pBig;
    private static float[] pSmallRed, pSmallBlue;
    private static int clockHeight;
    private static int scoreHeight;
    private static int score1X, score2X;
    private static float bigSize, smallSize;
    private static float xPos=0;
    private static float idealPos=0;
    private static String time;
    private static int lastTime;

    public static void reInit() {
        round=new LoopedTicker(LocalConfigs.getIntValue(LocalConfigs.timerTimer), new Runnable() {
            @Override
            public void run() {
                UnitLayer.killEverybody();
            }
        });

        pBig= LocalConfigs.getGrayFontColor();
        pSmallRed= LocalConfigs.getRedFontColor();
        pSmallBlue= LocalConfigs.getBlueFontColor();
    }

    public static void resize(int w, int h) {

        bigSize=w/4;
        smallSize=w/6;

        clockHeight =h/4;

        score1X =w/3;
        score2X =w*2/3;
        scoreHeight = (int) (clockHeight +bigSize);

    }

    public static void update(float dt) {
        round.tick(dt);
        if (Math.abs(xPos-idealPos)>=1f) {
             xPos+=(idealPos-xPos)*dt;
        }
        resetTime();
    }

    private static void resetTime() {
        int seconds= LocalConfigs.getIntValue(LocalConfigs.timerTimer)-(int)(round.getValue());
        if (lastTime!=seconds) {
            int minutes = seconds / 60;
            seconds %= 60;
            time = minutes + ":" + (seconds > 9 ? seconds : "0" + seconds);
            idealPos=getMiddleXUnits()-getStringWidth(bigSize, time)/2;
            lastTime = seconds;
        }
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.timerDraw)) {

            int[] kills = UnitLayer.getTeamSizes();
            Graphic.drawText(xPos, clockHeight, bigSize, pBig[0], pBig[1], pBig[2], pBig[3], time);

            String s1=String.valueOf(kills[1]);
            String s2=String.valueOf(kills[0]);

            Graphic.drawText(score1X - getStringWidth(smallSize, s1)/2, scoreHeight, smallSize,
                    pSmallRed[0], pSmallRed[1], pSmallRed[2], pSmallRed[3], s1);
            Graphic.drawText(score2X - getStringWidth(smallSize, s2)/2, scoreHeight, smallSize,
                    pSmallBlue[0], pSmallBlue[1], pSmallBlue[2], pSmallBlue[3], s2);
        }
    }
}
