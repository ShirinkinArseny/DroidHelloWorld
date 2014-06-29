package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;

public class TerritoryLayer{

    private static float[]  border;
    private static float[]  bg;
    private static float top, bottom, horizontal1, horizontal2;
    private static int resultMatrix1;
    private static int resultMatrix2;

    public static void reInit() {
        border= LocalConfigs.getWorldBoardersColor();
        bg= LocalConfigs.getWorldBGColor();
        resultMatrix1=Graphic.getResultMatrixID(0, 0,
                LocalConfigs.getDisplayWidth(), LocalConfigs.getDisplayHeight());
        resultMatrix2=Graphic.getResultMatrixID(horizontal1, top, bottom-top, horizontal2);
    }

    public static void resize(int w, int h) {
        top= LocalConfigs.getIntValue(LocalConfigs.worldVerticalTopBorders);
        bottom=h- LocalConfigs.getIntValue(LocalConfigs.worldVerticalBottomBorders);
        horizontal1= LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
        horizontal2=w-LocalConfigs.getIntValue(LocalConfigs.worldHorizontalBorders);
        resultMatrix1=Graphic.getResultMatrixID(0, 0, w, h);
        resultMatrix2=Graphic.getResultMatrixID(horizontal1, top, horizontal2-horizontal1, bottom-top);
    }

    public static void draw() {
        Graphic.bindResultMatrix(resultMatrix1);
        Graphic.drawRect(bg[0], bg[1], bg[2], bg[3]) ;

        Graphic.bindResultMatrix(resultMatrix2);
        Graphic.drawRect(border[0], border[1], border[2], border[3]);

        Graphic.unBindMatrices();
    }
}
