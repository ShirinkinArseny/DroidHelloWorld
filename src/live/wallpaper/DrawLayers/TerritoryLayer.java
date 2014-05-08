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
        border.setColor(Color.rgb(Configs.getWorldBoardersColor()[0], Configs.getWorldBoardersColor()[1],
                Configs.getWorldBoardersColor()[2]));
        border.setAlpha(Configs.getWorldBoardersColor()[3]);
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
        if (Configs.isWorldBoardersDraw())
        canvas.drawRect(Configs.getWorldHorizontalBorders(), Configs.getWorldVerticalTopBorders(),
                Configs.getDisplayWidth()-Configs.getWorldHorizontalBorders(),
                Configs.getDisplayHeight()-Configs.getWorldVerticalBottomBorders(), border);
    }
}
