package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public class Graphic {

    private static GraphicEngine g=new CanvasEngine();

    public static void startDraw() {
        g.startDraw();
    }

    public static void finishDraw() {
        g.finishDraw();
    }

    public static void drawBitmap(Bitmap b, float x, float y) {
        g.drawBitmap(b, x, y);
    }

    public static void drawBitmap(Bitmap b, float x, float y, int opacity) {
        g.drawBitmap(b, x, y, opacity);

    }

    public static void drawRect(float x, float y, float x2, float y2, int color, int opacity) {
        g.drawRect(x, y, x2, y2, color, opacity);
    }

    public static void drawText(float x, float y, float size, int color, String text) {
        g.drawText(x, y, size, color, text);
    }

    public static void drawText(float x, float y, float size, int color, int opacity, String text) {
        g.drawText(x, y, size, color, opacity, text);
    }

}
