package live.wallpaper.DrawLayers.MessagesLayer;

import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.OpenGLIntegration.Graphic;
import live.wallpaper.TimeFunctions.LinearTimeFunction;

public class Message {

    private String text;
    private LinearTimeFunction coordinates;
    private LinearTimeFunction alpha;
    private float x;
    private boolean isUseless=false;
    private int color;

    public boolean getUseless() {
        return isUseless;
    }

    public void update(float dt) {
        coordinates.tick(dt);
        alpha.tick(dt);
    }

    public Message(String text, float x, float y, int color) {
        this.x=x;
        coordinates =new LinearTimeFunction(Configs.getFloatValue(Configs.messageShowTime), y, y-50, new Runnable() {
            @Override
            public void run() {
                isUseless=true;
            }
        });
        alpha= new LinearTimeFunction(Configs.getFloatValue(Configs.messageShowTime), 255, 0, new Runnable() {
            @Override
            public void run() {
            }
        });
        this.text=text;
        this.color=color;
    }

    public void draw() {
            Graphic.drawText(x, coordinates.getValue(), 10f, color, (int) alpha.getValue(), text);
    }

}
