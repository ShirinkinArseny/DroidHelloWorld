package live.wallpaper.OpenGLIntegration.Shaders;

import android.graphics.Bitmap;
import live.wallpaper.Configs.LoggerConfig;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.texImage2D;


public class TextureGenerator {
    private static final String TAG = "TextureGenerator";

    public static int loadTexture(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0]==0) {
            LoggerConfig.w(TAG, "Could not generate new a new OpenGL texture object.");
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);



        glGenerateMipmap(GL_TEXTURE_2D);


        bitmap.recycle();


        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];

    }


}
