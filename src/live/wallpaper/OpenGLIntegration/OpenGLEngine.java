package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public class OpenGLEngine implements GraphicEngine {

    public OpenGLEngine() {
    }

    @Override
    public void startDraw() {
    }

    @Override
    public void finishDraw() {
    }

    @Override
    public int genTexture(Bitmap b) {
        return 0;
    }

    @Override
    public void drawBitmap(int b, float x, float y) {

    }

    @Override
    public void drawBitmap(int b, float x, float y, int opacity) {

    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, String text) {
    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {
    }

    @Override
    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
    }
}
