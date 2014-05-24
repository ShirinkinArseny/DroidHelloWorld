package live.wallpaper.DrawLayers.MessagesLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.LinearTimeFunction;

public class Message {

    private String text;
    private LinearTimeFunction coordinates;
    private LinearTimeFunction alpha;
    private float x;
    private boolean isUseless=false;
    private float r, g, b;

    public boolean getUseless() {
        return isUseless;
    }

    public void update(float dt) {
        coordinates.tick(dt);
        alpha.tick(dt);
    }

    public Message(String text, float x, float y, float[] rgb) {
        this.x=x;
        coordinates =new LinearTimeFunction(Configs.getFloatValue(Configs.messageShowTime), y, y-50, new Runnable() {
            @Override
            public void run() {
                isUseless=true;
            }
        });
        alpha= new LinearTimeFunction(Configs.getFloatValue(Configs.messageShowTime), 1, 0, new Runnable() {
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
            Graphic.drawText(x, coordinates.getValue(), 20f, r, g, b, alpha.getValue(), text);
    }

}
