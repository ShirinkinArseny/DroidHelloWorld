package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Message {

    private float[] deathes;
    private float delta;
    private String text;
    private Paint p;

    public boolean getNeedToRemove() {
        return deathes[2]<=0;
    }

    public void update(float dt) {
        deathes[2]-=delta*dt;
    }

    public Message(String text, float x, float y, Paint p) {
        deathes=new float[3];
        deathes[0]=x;
        deathes[1]=y;
        deathes[2]=1f;
        this.text=text;
        this.p=p;
        this.delta=1.5f;
    }

    public void draw(Canvas c) {
            p.setAlpha((int) (255*deathes[2]));
            c.drawText(text, deathes[0], deathes[1]-(1-deathes[2])*50, p);
    }

}
