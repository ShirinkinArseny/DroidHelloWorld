package com.acidspacecompany.epicwallpaperfight.OpenGLWrapping;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.acidspacecompany.epicwallpaperfight.R;

public class Font {

    private static final String map = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#%*()-+={}[]<>\'\\|/:;";

    private static final int[] dimensions = {152,137,140,141,131,131,145,145,67,127,142,124,169,145,147,138,147,139,142,140,141,150,186,143,143,135,130,92,131,130,137,127,131,132,133,132,67,198,144,169,120,93,94,91,139,120,100,100,83,93,121,123,65,112,77,110,71,71};
    private static int[] textures = new int[dimensions.length];
    private float scaleFactor;
    private int scaleMatrix=-1;
    private float size;
    private float[] sizedDimensions;

    public static void loadFont(Resources res) {

        int currentPosition = 0;
        Bitmap[] bitmaps = new Bitmap[dimensions.length];

        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter0));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter1));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter2));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter3));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter4));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter5));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter6));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter7));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter8));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter9));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter10));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter11));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter12));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter13));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter14));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter15));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter16));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter17));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter18));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter19));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter20));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter21));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter22));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter23));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter24));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter25));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter26));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter27));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter28));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter29));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter30));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter31));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter32));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter33));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter34));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter35));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter36));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter37));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter38));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter39));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter40));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter41));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter42));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter43));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter44));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter45));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter46));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter47));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter48));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter49));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter50));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter51));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter52));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter53));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter54));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter55));
        bitmaps[currentPosition++] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter56));
        bitmaps[currentPosition] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.letter57));

        textures = Graphic.genTextures(bitmaps);
    }

    public void drawString(String string, float x, float y, float r, float g, float b, float a) {
        int position;
        int lastPosition=-1;
        //Узнаем, на сколько нужно масштабировать
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                //Позиция символа в карте
                position = map.indexOf(string.charAt(i));
                if (lastPosition!=position)
                    Graphic.bindBitmap(textures[position]);
                Graphic.drawBitmap(x, y, r, g, b, a);
                x += sizedDimensions[position];
                lastPosition=position;
            } else {
                x += size;
            }
        }
    }

    public void prepareDraw() {
        Graphic.bindScaleMatrix(scaleMatrix);
    }

    public Font(float size) {
        resize(size);
    }

    public void resize(float size) {
        if (scaleMatrix!=-1)
            Graphic.cleanScaleMatrixID(scaleMatrix);
        scaleMatrix=Graphic.getScaleMatrixID(size, size);
        this.size=size;
        scaleFactor = size / 256f;
        sizedDimensions=new float[dimensions.length];
        for (int i=0; i<dimensions.length; i++) {
            sizedDimensions[i]=dimensions[i]*scaleFactor;
        }
    }

    public static float getStringWidth(float size, String string) {
        float x = 0.0f;
        int position;
        //Узнаем, на сколько нужно масштабировать
        final float scaleFactor = size / 256;
        float scaledWidth;
        char c;
        for (int i=0; i<string.length(); i++) {
            if ((c=string.charAt(i)) != ' ') {
                position = map.indexOf(c);
                scaledWidth = dimensions[position] * scaleFactor;
                x += scaledWidth;
            }
            else {
                x += size;
            }
        }
        return x;
    }
}
