package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

public class CanvaLayer {

    private static int bg;
    private static float dx;
    private static int width;

    public static void init(int b, int w) {
        bg = b;
        width = w;
        dx = 0;
    }

    public static void update(float dt) {
        if (LocalConfigs.getBooleanValue(LocalConfigs.canvaDraw)) {
            dx += dt * 50;
            if (dx > width) dx -= width;
        }
    }

    public static void draw() {
        if (LocalConfigs.getBooleanValue(LocalConfigs.canvaDraw))
        Graphic.fillBitmap(bg, width, dx);
    }
}
