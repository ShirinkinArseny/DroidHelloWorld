package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;

public class TerritoryLayer{

    private static Bitmap bg;//background picture
    private static Paint p;
    private static Paint border;

    public static void init(Bitmap b) {
        bg = b;
        p = new Paint();
        p.setColor(Color.WHITE);
        border = new Paint();
        border.setColor(Color.rgb(Configs.worldBoardersColor[0], Configs.worldBoardersColor[1],
                Configs.worldBoardersColor[2]));
        border.setAlpha(Configs.worldBoardersColor[3]);
        border.setStrokeWidth(0);
        border.setStyle(Paint.Style.STROKE);
    }

    public static void resize(int w, int h) {
        bg = Bitmap.createScaledBitmap(bg, w, h, true);
    }

    public static void update(float dt) {

    }

    public static void draw(Canvas canvas) {
        canvas.drawBitmap(bg, 0, 0, p);
        if (Configs.worldBoardersDraw)
        canvas.drawRect(Configs.worldHorizontalBorders, Configs.worldVerticalTopBorders,
                Configs.displayWidth-Configs.worldHorizontalBorders,
                Configs.displayHeight-Configs.worldVerticalBottomBorders, border);
    }
}
