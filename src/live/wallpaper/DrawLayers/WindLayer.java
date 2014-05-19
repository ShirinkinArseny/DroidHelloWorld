package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;

public class WindLayer {

    private static Bitmap bg;
    private static Paint p;
    private static float dx, dy;
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
        dy = 0;
    }

    public static void resize(int w, int h) {
        sx = w / width + 1;
        sy = h / height + 1;
    }

    public static void update(float dt) {
        if (Configs.getBooleanValue(Configs.windDraw)) {
            dx += dt * 50;
            dy += dt * 50;
            if (dx > width) dx -= width;
            if (dy > height) dy -= height;
        }
    }

    public static void draw(Canvas canvas) {
        if (Configs.getBooleanValue(Configs.windDraw)) {
            int posX = (int) (dx - width);
            for (int i = -1; i < sx; i++) {
                int posY = (int) (dy - height);
                for (int j = -1; j < sy; j++) {
                    canvas.drawBitmap(bg, posX, posY, p);
                    posY += height;
                }
                posX += width;
            }
        }
    }
}
