package live.wallpaper.DrawLayers;

import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;

public class TerritoryLayer{

    private static Paint p;
    private static float[]  border;
    private static float[]  bg;
    private static float top, bottom, horizontal1, horizontal2;

    public static void init() {
    }

    public static void reInit() {
        border=Configs.getWorldBoardersColor();
        bg=Configs.getWorldBGColor();
        resize();
    }

    public static void resize() {
        top=Configs.worldVerticalTopBorders;
        bottom=Configs.getDisplayHeight()-Configs.getIntValue(Configs.worldVerticalBottomBorders);
        horizontal1=Configs.worldHorizontalBorders;
        horizontal2=Configs.getDisplayWidth()-Configs.getIntValue(Configs.worldHorizontalBorders);
    }

    public static void update(float dt) {

    }

    public static void draw() {

        Graphic.drawRect(0, 0,
            Configs.getDisplayWidth(),
            Configs.getDisplayHeight(),
                bg[0], bg[1], bg[2], bg[3]) ;

        Graphic.drawRect(horizontal1,
                top, horizontal2, bottom,
                border[0], border[1], border[2], border[3]);
    }
}
