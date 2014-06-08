package com.acidspacecompany.epicwallpaperfight.OpenGLIntegration.Shaders.Generators;

import android.graphics.Bitmap;
import com.acidspacecompany.epicwallpaperfight.Configs.BicycleDebugger;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.texImage2D;


public class TextureGenerator {
    private static final String TAG = "TextureGenerator";

    public static int[] loadTextures(Bitmap[] bitmaps, boolean[] isInfinite) {
        final int[] textureObjectIds = new int[bitmaps.length];
        glGenTextures(bitmaps.length, textureObjectIds, 0);
        for (int i=0; i<textureObjectIds.length; i++) {
            if (textureObjectIds[i] == 0) {
                BicycleDebugger.w(TAG, "Could not generate new a new OpenGL texture object.");
                return null;
            }

            //Загружаем текстуру
            glBindTexture(GL_TEXTURE_2D, i);

            //Настраиваем стандартные параметры отрисовки
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            if (isInfinite[i]) {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            } else {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            }

            //Загружаем Bitmap
            texImage2D(GL_TEXTURE_2D, 0, bitmaps[i], 0);

            //Генерирем Mipmap
            glGenerateMipmap(GL_TEXTURE_2D);

            //Битмап не нужен больше ни кому
            bitmaps[i].recycle();


            //Сбрасываем текстуру так как действия с ней пока закончились
            glBindTexture(GL_TEXTURE_2D, 0);
        }
        return textureObjectIds;
    }

    public static int loadTexture(Bitmap bitmap, boolean isInfinite) {
        return loadTextures(new Bitmap[]{bitmap}, new boolean[]{isInfinite})[0];
    }

}
