package live.wallpaper.DrawLayers;

import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;

public class TerritoryLayer{

    private static Paint p;
    private static int border;
    private static int bg;

    public static void init() {
    }

    public static void reInit() {
        border=Color.rgb(Configs.getWorldBoardersColor()[0], Configs.getWorldBoardersColor()[1],
                Configs.getWorldBoardersColor()[2]);
        bg=Color.rgb(Configs.getWorldBGColor()[0], Configs.getWorldBGColor()[1],
                Configs.getWorldBGColor()[2]);
    }

    public static void update(float dt) {

    }

    public static void draw() {

        Graphic.drawRect(0, 0,
            Configs.getDisplayWidth(),
            Configs.getDisplayHeight(), bg,
            Configs.getWorldBGColor()[3]);

        Graphic.drawRect(Configs.getIntValue(Configs.worldHorizontalBorders),
                Configs.getIntValue(Configs.worldVerticalTopBorders),
                Configs.getDisplayWidth()-Configs.getIntValue(Configs.worldHorizontalBorders),
                Configs.getDisplayHeight()-Configs.getIntValue(Configs.worldVerticalBottomBorders), border,
                Configs.getWorldBoardersColor()[3]);
    }
}
