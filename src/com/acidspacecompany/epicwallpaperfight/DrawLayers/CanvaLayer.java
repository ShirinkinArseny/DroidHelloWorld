package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.World;

public class CanvaLayer {

    private static int bg;
    private static float dx;
    private static int width;
    private static int speed;
    private static Graphic.PaintingType fillType;

    private static boolean isBackgroungLoaded;
    private static Bitmap bitmapToLoad;

    public static void init(int w, int id, Graphic.PaintingType pt) {
        bg=id;
        width = w;
        dx = 0;
        fillType=pt;
        isBackgroungLoaded = true;
    }

    public static void init(int w) {
        width = w;
        dx = 0;
        isBackgroungLoaded = false;
    }

    public static void setTextureID(int id) {
        isBackgroungLoaded = true;
        bg=id;
    }

    public static void setPaintingType(Graphic.PaintingType pt) {
        fillType=pt;
    }


    public static void setDrawing(int id, Graphic.PaintingType pt) {
        isBackgroungLoaded = true;
        bg=id;
        fillType=pt;
    }

    public static void setDrawingBitmap(Bitmap drawingBitmap, Graphic.PaintingType paintingType) {
        isBackgroungLoaded = false;
        bitmapToLoad = drawingBitmap;
        fillType = paintingType;
        if (paintingType == Graphic.PaintingType.Tile)
            //Необходимо отрескейлить битмап так, чтобы он был квадратом со стороной кратной степени двойки
            resizeBitmapToFill();
    }

    private static void resizeBitmapToFill() {
        int rescaleWidth = 1;
        int bitmapSize = bitmapToLoad.getWidth();
        while (rescaleWidth < width & rescaleWidth < bitmapSize)
            rescaleWidth = rescaleWidth << 1;
        bitmapToLoad = World.getScaledBitmap(bitmapToLoad, rescaleWidth);
    }
    private static void loadBitmap() {
        if (fillType == Graphic.PaintingType.Tile)
            bg = Graphic.genInfinityTextureBackgroung(bitmapToLoad);
        else
            bg = Graphic.genTextureBackground(bitmapToLoad);
        isBackgroungLoaded = true;
    }

    public static void reInit() {
        speed=LocalConfigs.getIntValue(LocalConfigs.canvaSpeed);
    }

    public static void update(float dt) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.canvaDraw)) {
            dx += dt * speed;
            if (dx > width) dx -= width;
        }
    }

    public static void draw() {
        if (!isBackgroungLoaded)
            loadBitmap();
        if (LocalConfigs.getBooleanValue(LocalConfigs.canvaDraw))
        Graphic.fillBitmap(bg, width, dx, fillType);
    }
}
