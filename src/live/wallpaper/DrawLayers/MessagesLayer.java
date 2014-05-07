package live.wallpaper.DrawLayers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import live.wallpaper.Configs;

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
        pBlue.setColor(Color.rgb(Configs.blueFontColor[0], Configs.blueFontColor[1], Configs.blueFontColor[2]));
        pBlue.setTextSize(20f);

        pRed=new Paint();
        pRed.setAntiAlias(true);
        pRed.setColor(Color.rgb(Configs.redFontColor[0], Configs.redFontColor[1], Configs.redFontColor[2]));
        pRed.setTextSize(20f);

        syncer=new Synchroniser("MessagesLayer");
    }


    public static void showMessage(float x, float y, String text, int color) {
        if (Configs.messageDraw) {
        syncer.waitForUnlock();
        syncer.lock();
            messagesAddBuffer.add(
                    new Message(text, x, y, color == 0 ? pRed : pBlue)
            );
        syncer.unlock();
        }
    }

    public static void update(float dt) {
        if (Configs.messageDraw) {
            syncer.waitForUnlock();
            syncer.lock();
            for (int i = 0; i < messages.size(); i++) {
                messages.get(i).update(dt);
                if (messages.get(i).getNeedToRemove())
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
        if (Configs.messageDraw) {
            syncer.waitForUnlock();
            syncer.lock();
        for (Message f : messages)
            f.draw(canvas);
        syncer.unlock();
        }
    }
}
