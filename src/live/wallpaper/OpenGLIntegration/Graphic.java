package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public class Graphic {

    private static final GraphicEngine graphicEngine =new CanvasEngine();

    public static int genTexture(Bitmap b) {
        return graphicEngine.genTexture(b);
    }

    public static void startDraw() {
        graphicEngine.startDraw();
    }

    public static void finishDraw() {
        graphicEngine.finishDraw();
    }

    public static void drawBitmap(int b, float x, float y) {
        graphicEngine.drawBitmap(b, x, y);
    }

    public static void drawBitmap(int b, float x, float y, float opacity) {
        graphicEngine.drawBitmap(b, x, y, opacity);
    }

    public static void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        graphicEngine.drawRect(x, y, x2, y2, r, g, b, a);
    }

    public static void drawText(float x, float y, float size, float r, float g, float b, String text) {
        graphicEngine.drawText(x, y, size, r, g, b, text);
    }

    public static void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {
        graphicEngine.drawText(x, y, size, r, g, b, a, text);
    }

}
