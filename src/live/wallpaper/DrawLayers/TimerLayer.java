package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Ticker;

public class TimerLayer {

    private static Ticker round;
    private static Paint pBig;
    private static Paint pSmallRed, pSmallBlue;
    private static int width, height, height2, height2_3, width4, width2;

    public static void init() {
        round=new Ticker(60*5);
        pBig=new Paint();
        pBig.setColor(Color.rgb(128, 128, 128));
        pBig.setAlpha(64);
        pBig.setTextSize(300f);
        pSmallRed=new Paint();
        pSmallRed.setColor(Color.rgb(255, 64, 64));
        pSmallRed.setAlpha(64);
        pSmallRed.setTextSize(20f);
        pSmallBlue=new Paint();
        pSmallBlue.setColor(Color.rgb(64, 64, 255));
        pSmallBlue.setAlpha(64);
        pSmallBlue.setTextSize(20f);
    }

    public static void resize(int w, int h) {
        width=w;
        height=h;
        height2=h/2;
        height2_3=h*2/3;
        width4 =w/4;
        width2=w/2;
        pBig.setTextSize(w/4);
        pSmallRed.setTextSize(w/20);
        pSmallBlue.setTextSize(w/20);
    }

    public static void update(float dt) {
        round.tick(dt);
        if (round.getIsNextRound())
            UnitLayer.killEverybody();
    }

    private static String getTime() {
        int seconds=(int)(round.getTime());
        int minutes=seconds/60;
        seconds%=60;
        return minutes+":"+(seconds>10?seconds:"0"+seconds);
    }

    public static void draw(Canvas canvas) {
        canvas.drawText(getTime(), width4, height2, pBig);
        int[] kills=UnitLayer.getTeamSizes();
        canvas.drawText(String.valueOf(kills[1]), width2 -100, height2_3, pSmallRed);
        canvas.drawText(String.valueOf(kills[0]), width2, height2_3, pSmallBlue);
    }
}
