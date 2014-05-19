package live.wallpaper.DrawLayers.MessagesLayer;

import android.graphics.Canvas;
import android.graphics.Paint;
import live.wallpaper.Configs.Configs;
import live.wallpaper.TimeFunctions.LinearTimeFunction;

public class Message {

    private String text;
    private Paint p;
    private LinearTimeFunction coordinates;
    private LinearTimeFunction alpha;
    private float x;
    private boolean isUseless=false;

    public boolean getUseless() {
        return isUseless;
    }

    public void update(float dt) {
        coordinates.tick(dt);
        alpha.tick(dt);
    }

    public Message(String text, float x, float y, Paint p) {
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
        this.p=p;
    }

    public void draw(Canvas c) {
            p.setAlpha((int) (alpha.getValue()));
            c.drawText(text, x, coordinates.getValue(), p);
    }

}
