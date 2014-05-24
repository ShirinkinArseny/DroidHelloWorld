package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class CanvasEngine implements GraphicEngine {

    private Paint p;
    private static SurfaceHolder holder;
    private static Canvas c;

    public static void setHolder(SurfaceHolder h) {
          holder=h;
    }

    public CanvasEngine() {
         p=new Paint();
    }

    @Override
    public void startDraw() {
        c = holder.lockCanvas();
        p.setAntiAlias(true);
    }

    @Override
    public void finishDraw() {
        holder.unlockCanvasAndPost(c);
    }

    @Override
    public void drawBitmap(Bitmap b, float x, float y) {
        c.drawBitmap(b, x, y, null);
    }

    @Override
    public void drawBitmap(Bitmap b, float x, float y, int opacity) {
        p.setColor(Color.WHITE);
        p.setAlpha(opacity);
        c.drawBitmap(b, x, y, p);
    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, String text) {
        p.setColor(Color.rgb((int)(r*255), (int)(g*255), (int)(b*255)));
        p.setTextSize(size);
        c.drawText(text, x, y, p);
    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {
        p.setColor(Color.argb((int)(a*255), (int)(r*255), (int)(g*255), (int)(b*255)));
        p.setTextSize(size);
        c.drawText(text, x, y, p);
    }

    @Override
    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        p.setColor(Color.argb((int)(a*255), (int)(r*255), (int)(g*255), (int)(b*255)));
        c.drawRect(x, y, x2, y2, p);
    }
}
