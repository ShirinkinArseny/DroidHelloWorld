package live.wallpaper.OpenGLIntegration;

import android.content.Context;
import android.graphics.Bitmap;
import live.wallpaper.OpenGLIntegration.Shaders.ShaderGenerator;
import live.wallpaper.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import static android.opengl.GLES20.*;

public class OpenGLES20Engine implements GraphicEngine {

    //В float 4 байта
    private static final int BYTES_PER_FLOAT = 4;
    //Вершины двухмерные
    private static final int POSITION_COMPONENT_COUNT = 2;

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
    public void drawText(float x, float y, float size, float r, float g, float b, String text) {

    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {

    }

    @Override
    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        float sizeX = x2-x;
        float sizeY = y2-y;
        //Создаем массив вершин
        FloatBuffer nativeVertexes = createNativeFloatArray(new float[]{
                //Верхний левый угол
                x, y2,
                //Верхний правый угол
                x2,y2,
                //Нижний правый угол
                x2,y,
                //Нижний левый угол
                x,y
        });
        //Используем программу простой заливки
        glUseProgram(fillColorShader);
        final int aPosition = glGetAttribLocation(fillColorShader, "a_Position");
        //Устанавливаем указатель на начало массива
        nativeVertexes.position(0);
        //Задаем алгоритм обхода вершин в массиве
        glVertexAttribPointer(
                //Атрибут задан
                aPosition,
                //Количество компонент в вершине равно 2 (2D)
                POSITION_COMPONENT_COUNT,
                //Все значения имеют тип float
                GL_FLOAT,
                //Значения не должны быть нормализированы
                false,
                //После вершины пробел для каких-либо ещё величин в массиве делать не нужно т.к. их нет
                0,
                //Все данные о вершинах лежат в nativeVertexes
                nativeVertexes);
        //Заюзали
    }

}
