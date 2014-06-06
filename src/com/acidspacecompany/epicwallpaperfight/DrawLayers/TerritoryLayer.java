package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLIntegration.Graphic;

public class TerritoryLayer{

    private static float[]  border;
    private static float[]  bg;
    private static float top, bottom, horizontal1, horizontal2;

    public static void reInit() {
        border= LocalConfigs.getWorldBoardersColor();
        bg= LocalConfigs.getWorldBGColor();
    }

    public static void resize(int w, int h) {
        top= h*LocalConfigs.getFloatValue(LocalConfigs.worldVerticalTopBorders);
        bottom=h*(1- LocalConfigs.getFloatValue(LocalConfigs.worldVerticalBottomBorders));
        horizontal1= w*LocalConfigs.getFloatValue(LocalConfigs.worldHorizontalBorders);
        horizontal2=w*(1-LocalConfigs.getFloatValue(LocalConfigs.worldHorizontalBorders));
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
