package live.wallpaper.DrawLayers;

import live.wallpaper.Configs.LocalConfigs;
import live.wallpaper.OpenGLIntegration.Graphic;

public class TerritoryLayer{

    private static float[]  border;
    private static float[]  bg;
    private static float top, bottom, horizontal1, horizontal2;

    public static void reInit() {
        border= LocalConfigs.getWorldBoardersColor();
        bg= LocalConfigs.getWorldBGColor();
    }

    public static void resize(int w, int h) {
        top= LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders);
        bottom=h- LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders);
        horizontal1= LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
        horizontal2=w- LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
    }

    public static void draw() {

        Graphic.drawRect(0, 0,
            LocalConfigs.getDisplayWidth(),
            LocalConfigs.getDisplayHeight(),
                bg[0], bg[1], bg[2], bg[3]) ;

        Graphic.drawRect(horizontal1,
                top, horizontal2, bottom,
                border[0], border[1], border[2], border[3]);
    }
}
