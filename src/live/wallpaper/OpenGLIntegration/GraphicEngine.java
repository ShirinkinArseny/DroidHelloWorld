package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public interface GraphicEngine {

    public void startDraw();

    public void finishDraw();

    public void drawBitmap(Bitmap b, float x, float y);

    public void drawBitmap(Bitmap b, float x, float y, int opacity);

    public void drawText(float x, float y, float size, int color, String text);

    public void drawText(float x, float y, float size, int color, int opacity, String text);

    public void drawRect(float x, float y, float x2, float y2, int color, int opacity);
}
