package live.wallpaper.OpenGLIntegration;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import live.wallpaper.OpenGLIntegration.Shaders.ShaderGenerator;
import live.wallpaper.OpenGLIntegration.Shaders.TextureGenerator;
import live.wallpaper.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import static android.opengl.GLES20.*;

public class OpenGLES20Engine implements GraphicEngine {

    //Создание матрицы преобразования для вывода на экран с использованием абсолютной системы координат
    //Собственно матрица (матрица проекции)
    private static FloatBuffer projectionMatrix;
    public static void updateScreen(int width, int height) {
        //Задаем режим вывода для OpenGL
        glViewport(0, 0, width, height);
        //Создаем матрицу

        //Масштаб
        float[] scale = new float[16];
        Matrix.setIdentityM(scale,0);
        Matrix.scaleM(scale, 0, 3.0f*width/2.0f, 3.0f*height/2.0f, 0f);
        //Перенос
        float[] translation = new float[16];
        Matrix.setIdentityM(translation,0);
        Matrix.translateM(translation, 0, -0.5f, -0.5f, 0f);
        //Произведение преобразований
        float[] result = new float[16];
        Matrix.multiplyMM(result,0,scale,0,translation,0);
        projectionMatrix = createNativeFloatArray(result);
        projectionMatrix.position(0);
    }


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
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,0.0f);
    }

    @Override
    public void finishDraw() {

    }

    @Override
    public int genTexture(Bitmap b) {
        return TextureGenerator.loadTexture(b);
    }

    @Override
    public void drawBitmap(int b, float x, float y) {

    }

    @Override
    public void drawBitmap(int b, float x, float y, int opacity) {

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
        //Рисовать можно ТОЛЬКО треугольники, а поэтому в массив записаны вершины
        //Двух треугольников (но потом это выпиливается к херам и приделывется два массива
        //первый будет состоять из 4 float (вершин), а второй из 6 int (порядок обхода)
        final FloatBuffer nativeVertexes = createNativeFloatArray(new float[]{
                //Нижний левый угол
                x,y,
                //Верхний левый угол
                x, y2,
                //Верхний правый угол
                x2,y2,
                //Нижний левый угол
                x,y,
                //Верхний правый угол
                x2,y2,
                //Нижний правый угол
                x2,y
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
        //Заюзали атрибут
        glEnableVertexAttribArray(aPosition);
        //Задаем матрицу для того чтобы отойти от экранной СК
        final int uMatrix = glGetUniformLocation(fillColorShader, "u_Matrix");
        projectionMatrix.position(0);
        glUniformMatrix4fv(uMatrix, 1, false, projectionMatrix);

        //Задаем цвет
        //Получаем uniform для шейдра
        final int uColor = glGetUniformLocation(fillColorShader, "u_Color");
        //Собственно указываем цвет
        glUniform4f(uColor, r,g,b,a);


        //Итак: было сделано дохера, но теперь. Мы.
        //Отрисовываем всё к херам

        //И опять про то, почему именно GL_TRIANGLES
        //GL_QUADS и все остальное были выпилены
        //так как по-сути видеокарта рисует только треугольники.
        //Использование GL_QUADS проще с моей точки зрения
        //так как мне меньше писать, но с точки зрения машины это ни разу
        //не быстрее так за использованием
        //GL_QUADS сокрыта куча кода, которой быть не должно`
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
