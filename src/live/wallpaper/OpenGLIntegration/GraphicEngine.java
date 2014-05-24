package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public interface GraphicEngine {

    public void startDraw();

    public void finishDraw();

    public void drawBitmap(Bitmap b, float x, float y);

    public void drawBitmap(Bitmap b, float x, float y, int opacity);

    public void drawText(float x, float y, float size, float r, float g, float b, String text);

    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text);

    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a);
}
