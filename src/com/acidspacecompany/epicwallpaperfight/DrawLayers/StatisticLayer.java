package com.acidspacecompany.epicwallpaperfight.DrawLayers;

import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Font;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.World;

public class StatisticLayer {

    private static Font font;

    public static void init() {
        font=new Font(19f);
    }

    public static void draw() {
        font.prepareDraw(0, 0, 0, 1);
        font.drawString("TPU: "+ World.getTPU()+" MS", 10, 60);
        font.drawString("MEAN TPU: "+ World.getLowPassedTPU()+" MS", 10, 80);
        font.drawString("FPS: "+ World.getFPS(), 10, 100);
        font.drawString("MEAN FPS: "+ World.getLowPassedFPS(), 10, 120);
        font.drawString("SCALE  MATRICES: "+ Graphic.getScaleMatricesCount(), 10, 140);
        font.drawString("USED SCALE  MATRICES: "+ Graphic.getUsedScaleMatricesCount(), 10, 160);
        font.drawString("RESULT MATRICES: "+ Graphic.getResultMatricesCount(), 10, 180);
        font.drawString("USED RESULT MATRICES: "+ Graphic.getUsedResultMatricesCount(), 10, 200);
    }
}
