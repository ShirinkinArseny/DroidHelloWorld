package live.wallpaper.DrawLayers;

import live.wallpaper.OpenGLIntegration.Graphic;

public class CanvaLayer {

    private static int bg;
    private static float dx;
    private static int width;
    private static int height;
    private static int sx, sy;

    public static void init(int b, int w, int h) {
        bg = b;
        width = w;
        height = h;
        dx = 0;
    }

    public static void resize(int w, int h) {
        sx = w;
        sy = h;
    }

    public static void update(float dt) {
            dx += dt * 50;
            if (dx > width) dx -= width;
    }

    public static void draw() {
                    Graphic.fillBitmap(bg, width, dx);
    }
}
