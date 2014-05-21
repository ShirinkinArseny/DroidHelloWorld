package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;

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
        sx = w / width + 1;
        sy = h / height + 1;
    }

    public static void update(float dt) {
            dx += dt * 50;
            if (dx > width) dx -= width;
    }

    public static void draw(Canvas canvas) {
            float posX = dx - width;
            for (int i = -1; i < sx; i++) {
                float posY = - height;
                for (int j = -1; j < sy; j++) {
                    canvas.drawBitmap(bg, posX, posY, p);
                    posY += height;
                }
                posX += width;
            }
    }
}
