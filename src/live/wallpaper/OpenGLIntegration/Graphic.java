package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;
import live.wallpaper.Geometry.Rectangle;

public class Graphic {

    private static final GraphicEngine graphicEngine =new OpenGLES20Engine();

    public static int genTexture(Bitmap b) {
        return graphicEngine.genTexture(b);
    }

    public static void startDraw() {
        graphicEngine.startDraw();
    }

    public static void finishDraw() {
        graphicEngine.finishDraw();
    }

    public static void fillBitmap(int b, int w, float dx) {
        //TODO: замостить всё битмапом с шириной и высотой w и отклонением по оси x dx
    }

    public static void drawBitmap(int b, Rectangle r) {
        //TODO: там всё сделать нормально graphicEngine.drawBitmap(b, r);
    }

    public static void drawBitmap(int b, Rectangle r, float opacity) {
        //TODO: там всё сделать нормально graphicEngine.drawBitmap(b, x, y, opacity);
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
