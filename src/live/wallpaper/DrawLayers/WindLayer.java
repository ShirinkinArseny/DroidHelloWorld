package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
public class WindLayer {

    private static Bitmap bg;
    private static Paint p;
    private static float dx;
    private static int width;
    private static int height;
    private static int sx, sy;

    public static void init(Bitmap b) {
        bg = b;
        width = b.getWidth();
        height = b.getHeight();
        p = new Paint();
        p.setColor(Color.WHITE);
        dx = 0;
    }

    public static void resize(int w, int h) {
        sx = w;
        sy = h;
    }

    public static void update(float dt) {
            dx += dt * 50;
            if (dx > width) dx -= width;
    }

    public static void draw(Canvas canvas) {
            for (float i = dx - width; i < sx; i+=width) {
                for (float j = 0; j < sy; j+=height) {
                    canvas.drawBitmap(bg, i, j, p);
                }
            }
    }
}
