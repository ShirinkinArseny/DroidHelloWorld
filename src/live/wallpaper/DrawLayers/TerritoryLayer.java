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
        border.setColor(Color.WHITE);
        border.setAlpha(32);
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
        canvas.drawRect(Configs.worldBorders, Configs.worldBorders,
                Configs.displayWidth-Configs.worldBorders,
                Configs.displayHeight-Configs.worldBorders, border);
    }
}
