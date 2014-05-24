package live.wallpaper.OpenGLIntegration;

import android.content.Context;
import android.graphics.Bitmap;
import live.wallpaper.OpenGLIntegration.Shaders.ShaderGenerator;
import live.wallpaper.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class OpenGLES20Engine implements GraphicEngine {

    private static final int BYTES_PER_FLOAT = 4;

    /**
     * Метод, создающий нативный массив значений float из массива Java
     * @param javaFloatArray Массив Java
     * @return Нативный массив
     */
    private static FloatBuffer createNativeFloatArray(float[] javaFloatArray)
    {
        //Создаем ByteBuffer
        return ByteBuffer.
                //Теперь задаем размер таким, чтобы влезли все величины
                allocateDirect(javaFloatArray.length * BYTES_PER_FLOAT).
                //Задаем порядок, родной для машины (прямой (0x01) или перевернутый (0x10))
                order(ByteOrder.nativeOrder()).
                //Храним его как массив значений float
                asFloatBuffer().
                //Добавляем значения из массива
                put(javaFloatArray);
    }

    private static int fillColorShader;

    public static void init(Context context) {
        fillColorShader = ShaderGenerator.createProgram(context,
                R.raw.fillcolor_vertex_shader,
                R.raw.fillcolor_fragment_shader);
    }

    @Override
    public void startDraw() {

    }

    @Override
    public void finishDraw() {

    }

    @Override
    public void drawBitmap(Bitmap b, float x, float y) {

    }

    @Override
    public void drawBitmap(Bitmap b, float x, float y, int opacity) {

    }

    @Override
    public void drawText(float x, float y, float size, int color, String text) {

    }

    @Override
    public void drawText(float x, float y, float size, int color, int opacity, String text) {

    }

    @Override
    public void drawRect(float x, float y, float x2, float y2, int color, int opacity) {

    }
}
