package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Font;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LoopedTicker;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.NiceMoveTimeFunction;

import static com.acidspacecompany.epicwallpaperfight.DrawLayers.UnitLayer.getMiddleXUnits;
import static com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Font.getStringWidth;

public class TimerLayer {

    private static LoopedTicker round;
    private static float[] pBig;
    private static float[] pSmallRed, pSmallBlue;
    private static int clockHeight;
    private static int scoreHeight;
    private static float bigSize, smallSize;

    private static NiceMoveTimeFunction timePosition =new NiceMoveTimeFunction(0, 0, 10f);
    private static NiceMoveTimeFunction score1Position =new NiceMoveTimeFunction(0, 0, 10f);
    private static NiceMoveTimeFunction score2Position =new NiceMoveTimeFunction(0, 0, 10f);

    private static Font big=new Font(10f);
    private static Font small=new Font(10f);

    private static String time="";
    private static String s1="";
    private static String s2="";
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
        scoreHeight = (int) (clockHeight +bigSize);
        big.resize(bigSize);
        small.resize(smallSize);
    }

    public static void update(float dt) {
        round.tick(dt);

        timePosition.tick(dt);
        score1Position.tick(dt);
        score2Position.tick(dt);

        updateTime();
    }

    public static void setTimer(float time) {
        round=new LoopedTicker(time, new Runnable() {
            @Override
            public void run() {
                UnitLayer.killEverybody();
            }
        });
    }

    private static void updateTime() {
        int seconds= (int)(round.getValue());
        if (lastTime!=seconds) {
            int minutes = seconds / 60;
            seconds %= 60;
            time = minutes + ":" + (seconds > 9 ? seconds : "0" + seconds);
            float mid=getMiddleXUnits();
            float s1width_15=1.2f*getStringWidth(smallSize, s1);
            score1Position.setAim(mid-s1width_15);
            score2Position.setAim(score1Position.getValue()+s1width_15+getStringWidth(smallSize, s2)*0.2f);
            timePosition.setAim(mid - getStringWidth(bigSize, time) / 2);
            lastTime = seconds;
        }
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.timerDraw)) {

            int[] kills = UnitLayer.getTeamSizes();
            big.prepareDraw();
            big.drawString(time, timePosition.getValue(), clockHeight, pBig[0], pBig[1], pBig[2], pBig[3]);

            s1=String.valueOf(kills[1]);
            s2=String.valueOf(kills[0]);

            small.prepareDraw();
            small.drawString(s1, score1Position.getValue(), scoreHeight,
                    pSmallRed[0], pSmallRed[1], pSmallRed[2], pSmallRed[3]);
            small.drawString(s2, score2Position.getValue(), scoreHeight,
                    pSmallBlue[0], pSmallBlue[1], pSmallBlue[2], pSmallBlue[3]);
        }
    }
}
