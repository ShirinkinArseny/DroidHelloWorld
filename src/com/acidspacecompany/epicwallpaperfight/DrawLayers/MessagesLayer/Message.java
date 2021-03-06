package com.acidspacecompany.epicwallpaperfight.DrawLayers.MessagesLayer;

import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Font;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.TimeFunctions.LinearTimeFunction;

import static com.acidspacecompany.epicwallpaperfight.World.getScaledValue;

public class Message {

    private final String text;
    private final LinearTimeFunction coordinates;
    private final LinearTimeFunction alpha;
    private final float x;
    private boolean isUseless=false;
    private final float r;
    private final float g;
    private final float b;
    private static float size;
    private static Font font;
    private static float deltaHeight;

    public boolean getUseless() {
        return isUseless;
    }

    public void update(float dt) {
        coordinates.tick(dt);
        alpha.tick(dt);
    }

    public Message(String text, float x, float y, float[] rgb) {
        this.x=x;
        coordinates =new LinearTimeFunction(LocalConfigs.getFloatValue(LocalConfigs.messageShowTime), y, y-deltaHeight, new Runnable() {
            @Override
            public void run() {
                isUseless=true;
            }
        });
        alpha= new LinearTimeFunction(LocalConfigs.getFloatValue(LocalConfigs.messageShowTime), 1, 0, new Runnable() {
            @Override
            public void run() {
            }
        });
        this.text=text;
        this.r=rgb[0];
        this.g=rgb[1];
        this.b=rgb[2];
    }

    public void draw() {
        font.drawString(text, x, coordinates.getValue(), r, g, b, alpha.getValue());
    }

    public void prepareDraw() {
        font.prepareDraw();
    }

    public static void init() {
        size=getScaledValue(50);
        deltaHeight=getScaledValue(65);
        font=new Font(size);
    }
}
