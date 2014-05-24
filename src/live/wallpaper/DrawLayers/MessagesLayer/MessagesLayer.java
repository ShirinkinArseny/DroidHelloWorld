package live.wallpaper.DrawLayers.MessagesLayer;

import live.wallpaper.Configs.Configs;
import live.wallpaper.DrawLayers.Synchroniser;
import live.wallpaper.Geometry.Point;

import java.util.LinkedList;

public class MessagesLayer{

    private static final LinkedList<Message> messages=new LinkedList<>();//coordinates of messages
    private static final LinkedList<Message> messagesAddBuffer=new LinkedList<>();//coordinates of messages
    private static float[] pBlue; //Blue paint
    private static float[] pRed; //Red paint
    private static Synchroniser syncer;

    public static void init() {

        pBlue=Configs.getBlueFontColor();

        pRed=Configs.getRedFontColor();

        syncer=new Synchroniser("MessagesLayerSync");
    }

    public static void reInit() {
        pBlue=Configs.getBlueFontColor();
        pRed=Configs.getRedFontColor();
    }

    public static void showMessage(float x, float y, String text, int color) {
        if (Configs.getBooleanValue(Configs.messageDraw)) {
        syncer.waitForUnlockAndLock();
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
        if (Configs.getBooleanValue(Configs.messageDraw)) {
            syncer.waitForUnlockAndLock();
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

    public static void draw() {
        if (Configs.getBooleanValue(Configs.messageDraw)) {
            syncer.waitForUnlockAndLock();
        for (Message f : messages)
            f.draw();
        syncer.unlock();
        }
    }
}
