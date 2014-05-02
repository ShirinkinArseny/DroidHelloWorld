package live.wallpaper.DrawLayers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

public class BloodLayer{

    private static LinkedList<float[]> dust;//blood coordinates
    private static Bitmap dustTexture[];//blood texture
    private static Paint p;

    public static void init(Bitmap[] pics) {
        dust=new LinkedList<>();
        dustTexture=pics;
        p=new Paint();
        p.setColor(Color.WHITE);
    }

    public static void add(float x, float y, float val, float type) {
        dust.add(new float[]{x, y, val, type});
    }

    public static void update(float dt) {
        for (int i=0; i<dust.size(); i++) {
            dust.get(i)[2]-=5*dt;
            if (dust.get(i)[2]<=0)
                dust.remove(i);
        }
    }

    public static void draw(Canvas canvas) {
        for (float[] f: dust) {
            canvas.drawBitmap(dustTexture[((int) f[3])], f[0], f[1], p);
        }
    }
}
