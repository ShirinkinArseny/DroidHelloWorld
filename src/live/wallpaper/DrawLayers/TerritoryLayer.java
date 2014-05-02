package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TerritoryLayer{

    private static Bitmap bg;//background picture
    private static Paint p;

    public static void init(Bitmap b) {
         bg=b;
        p=new Paint();
        p.setColor(Color.WHITE);
    }

    public static void resize(int w, int h) {
        bg = Bitmap.createScaledBitmap(bg, w, h, true);
    }

    public static void update(float dt) {

    }

    public static void draw(Canvas canvas) {
        canvas.drawBitmap(bg, 0, 0, p);
    }
}
