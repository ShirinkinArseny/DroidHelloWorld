package com.acidspacecompany.epicwallpaperfight.OpenGLIntegration.Shaders.Generators;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.acidspacecompany.epicwallpaperfight.OpenGLIntegration.Graphic;
import com.acidspacecompany.epicwallpaperfight.R;

public class FontLoader {

    private static final int letterNo = 58;

    public static int[] loadFont(Resources res) {
        int currentPosition = 0;
        Bitmap[] bitmaps = new Bitmap[letterNo];

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

        return Graphic.genTextures(bitmaps);
    }

}
