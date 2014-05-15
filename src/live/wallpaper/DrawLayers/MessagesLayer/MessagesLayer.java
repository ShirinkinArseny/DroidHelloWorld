package live.wallpaper.DrawLayers.MessagesLayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;
import live.wallpaper.DrawLayers.Synchroniser;
import live.wallpaper.Geometry.Point;

import java.util.LinkedList;

public class MessagesLayer{

    private static final LinkedList<Message> messages=new LinkedList<>();//coordinates of messages
    private static final LinkedList<Message> messagesAddBuffer=new LinkedList<>();//coordinates of messages
    private static Paint pBlue; //Blue paint
    private static Paint pRed; //Red paint
    private static Synchroniser syncer;

    public static void init() {

        pBlue=new Paint();
        pBlue.setAntiAlias(true);
        pBlue.setColor(Color.rgb(Configs.getBlueFontColor()[0], Configs.getBlueFontColor()[1], Configs.getBlueFontColor()[2]));
        pBlue.setTextSize(20f);

        pRed=new Paint();
        pRed.setAntiAlias(true);
        pRed.setColor(Color.rgb(Configs.getRedFontColor()[0], Configs.getRedFontColor()[1], Configs.getRedFontColor()[2]));
        pRed.setTextSize(20f);

        syncer=new Synchroniser();
    }


    public static void showMessage(float x, float y, String text, int color) {
        if (Configs.isMessageDraw()) {
        syncer.waitForUnlock();
        syncer.lock();
            messagesAddBuffer.add(
                    new Message(text, x, y, color == 0 ? pRed : pBlue)
            );
        syncer.unlock();
        }
    }

    public static void showMessage(Point p, String text, int color) {
        showMessage(p.getX(), p.getY(), text, color);
    }

    public static void update(float dt) {
        if (Configs.isMessageDraw()) {
            syncer.waitForUnlock();
            syncer.lock();
            for (int i = 0; i < messages.size(); i++) {
                messages.get(i).update(dt);
                if (messages.get(i).getUseless())
                    messages.remove(i);
            }
            while (!messagesAddBuffer.isEmpty()) {
                messages.add(messagesAddBuffer.get(0));
                messagesAddBuffer.remove(0);
            }
            syncer.unlock();
        }
    }

    public static void draw(Canvas canvas) {
        if (Configs.isMessageDraw()) {
            syncer.waitForUnlock();
            syncer.lock();
        for (Message f : messages)
            f.draw(canvas);
        syncer.unlock();
        }
    }
}
