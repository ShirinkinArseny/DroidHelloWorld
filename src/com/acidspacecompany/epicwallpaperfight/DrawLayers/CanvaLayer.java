package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

public class CanvaLayer {

    private static int bg;
    private static float dx;
    private static int width;
    private static int speed;

    public static void init(int b, int w) {
        bg = b;
        width = w;
        dx = 0;
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
        if (LocalConfigs.getBooleanValue(LocalConfigs.canvaDraw))
        Graphic.fillBitmap(bg, width, dx);
    }
}
