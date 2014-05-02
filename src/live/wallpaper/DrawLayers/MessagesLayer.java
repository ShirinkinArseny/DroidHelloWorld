package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

public class MessagesLayer{

    private static final LinkedList<Message> messages=new LinkedList<>();//coordinates of messages
    private static Paint pBlue; //Blue paint
    private static Paint pRed; //Red paint

    public static void init() {

        pBlue=new Paint();
        pBlue.setColor(Color.rgb(128, 128, 255));
        pBlue.setAlpha(128);
        pBlue.setTextSize(20f);

        pRed=new Paint();
        pRed.setColor(Color.rgb(255, 128, 128));
        pRed.setAlpha(128);
        pRed.setTextSize(20f);
    }


    public static void showMessage(float x, float y, String text, int color) {
        messages.add(
                new Message(text, x, y, color == 0 ? pRed : pBlue)
        );
    }

    public static void update(float dt) {
        for (int i=0; i< messages.size(); i++) {
            messages.get(i).update(dt);
            if (messages.get(i).getNeedToRemove())
                messages.remove(i);
        }
    }

    public static void draw(Canvas canvas) {
        for (Message f : messages)
            f.draw(canvas);
    }
}
