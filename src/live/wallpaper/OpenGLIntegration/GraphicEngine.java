package live.wallpaper.OpenGLIntegration;

import android.graphics.Bitmap;

public interface GraphicEngine {

    public void startDraw();

    public void finishDraw();

    /**
     * Генерирует текстуру и возвращает её итентификатор для последующей отрисовки
     * @param b Битмап, из которого создается текстура (величина сторон должна быть степенью двойки!)
     * @return Итентификатор для последующей отрисовки
     */
    public int genTexture(Bitmap b);

    //Здесь и далее текстура задается итентификатором
    public void drawBitmap(int b, float x, float y);

    public void drawBitmap(int b, float x, float y, float opacity);

    public void drawText(float x, float y, float size, float r, float g, float b, String text);

    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text);

    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a);
}
