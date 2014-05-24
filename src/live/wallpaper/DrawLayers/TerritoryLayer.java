package live.wallpaper.DrawLayers;

import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;

public class TerritoryLayer{

    private static float[]  border;
    private static float[]  bg;
    private static float top, bottom, horizontal1, horizontal2;

    public static void reInit() {
        border=Configs.getWorldBoardersColor();
        bg=Configs.getWorldBGColor();
    }

    public static void resize(int w, int h) {
        top=Configs.getIntValue(Configs.worldVerticalTopBorders);
        bottom=h-Configs.getIntValue(Configs.worldVerticalBottomBorders);
        horizontal1=Configs.getIntValue(Configs.worldHorizontalBorders);
        horizontal2=w-Configs.getIntValue(Configs.worldHorizontalBorders);
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
