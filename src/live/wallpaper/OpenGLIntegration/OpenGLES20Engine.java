package live.wallpaper.OpenGLIntegration;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import live.wallpaper.Configs.LoggerConfig;
import live.wallpaper.OpenGLIntegration.Shaders.ShaderGenerator;
import live.wallpaper.OpenGLIntegration.Shaders.TextureGenerator;
import live.wallpaper.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;


import static android.opengl.GLES20.*;

public class OpenGLES20Engine implements GraphicEngine {

    //Хранение размеров Bitmap по его ключу-итентификатору
    //TODO: выпилить нафиг
    private static Map textureDimensions;

    //Создание матрицы преобразования для вывода на экран с использованием абсолютной системы координат
    //Собственно матрица (матрица проекции)
    private static FloatBuffer projectionMatrix;

    /**
     * Включает использование матрицы преобразования в шейдере
     * @param programId Программа шейдера
     */
    private static void useMatrix(int programId) {
        //Задаем матрицу для того чтобы отойти от экранной СК
        projectionMatrix.position(0);
        glUniformMatrix4fv(glGetUniformLocation(programId, "u_Matrix"), 1, false, projectionMatrix);
    }

    /**
     * Обновление параметров матрицы преобразования. Должна быть вызвана при изменении параметров экрана
     * @param width Ширина
     * @param height Высота
     */
    public static void updateScreen(int width, int height) {
        glViewport(0,0,width,height);

        float[] result = new float[16];
        Matrix.orthoM(result, 0, 0,width,height,0,-1,1);

        projectionMatrix = createNativeFloatArray(result);
        projectionMatrix.position(0);

    }


    //В float 4 байта
    private static final int BYTES_PER_FLOAT = 4;
    //Вершины двухмерные
    private static final int POSITION_COMPONENT_COUNT = 2;
    //В текстуре 2 координаты
    private static final int UV_COMPONENT_COUNT = 2;

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

    //Шейдер, который просто заливает всё цветом
    private static int fillColorShader;
    //Шейдер, который заливает всё текстурой и прозрачностью
    private static int textureShader;

    /**
     * Инициализация системы. Компилирование всего и вся.
     * @param context Контекст, в котором содержатся все ресурсы
     */
    public static void init(Context context) {
        //Загружаем шейдеры
        fillColorShader = ShaderGenerator.createProgram(context,
                R.raw.fillcolor_vertex_shader,
                R.raw.fillcolor_fragment_shader);
        ShaderGenerator.validateProgram(fillColorShader);
        textureShader = ShaderGenerator.createProgram(context,
                R.raw.texture_vertex_shader,
                R.raw.texture_fragment_shader);
        ShaderGenerator.validateProgram(textureShader);
        //Создаем словарь с размерами Bitmap
        textureDimensions = new TreeMap<Integer, float[]>();


        //Включаем alpha-blending

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
    }

    /**
     * Преобразование массива Object[] из ключей Map в массив int[]
     * @param array Массив ключей из Map
     * @return Массив ключей в int
     */
    private static int[] convert(Object[] array) {
        int[] result = new int[array.length];

        for (int i=0; i<result.length; i++)
            result[i] = (int)array[i];

        return result;
    }

    /**
     * Уничтожение контекста
     */
    public static void destroy() {
        glDeleteProgram(fillColorShader);
        glDeleteProgram(textureShader);
        glDeleteTextures(textureDimensions.size(), convert(textureDimensions.keySet().toArray()),0);
    }

    /**
     * Начало отрисовки. Очищение экрана белым цветом.
     */
    @Override
    public void startDraw() {
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void finishDraw() {

    }

    /**
     * Генерация текстуры из Bitmap
     * @param b Битмап, из которого создается текстура (величина сторон должна быть степенью двойки!)
     * @return Итентификатор
     */
    @Override
    public int genTexture(Bitmap b) {
        //LoggerConfig.d("OpenGLEngine", b.isRecycled()+"");
        final int id = TextureGenerator.loadTexture(b);
        textureDimensions.put(id, new float[]{b.getWidth(), b.getHeight()});
        return id;
    }

    @Override
    public void drawBitmap(int b, float x, float y) {
        drawBitmap(b,x,y,1.0f);
    }

    @Override
    public void drawBitmap(int b, float x, float y, float opacity) {
        //Получаем высоту
        final float[] dimensions = (float[])textureDimensions.get(b);
        final float width = dimensions[0];
        final float height = dimensions[1];

        //Вершины со всеми значениями
        final FloatBuffer nativeVertexes = createNativeFloatArray(new float[]{
                //Порядок: вершины, текстурные координаты
                //Левый нижний угол
                x,y, 0,0,
                //Правый верхний угол
                x+width, y+height, 1,1,
                //Левый верхний угол
                x,y+height, 0,1,
                //Правый нижний угол
                x+width, y, 1,0,
                //Левый нижний угол
                x,y, 0,0,
                //Правый верхний угол
                x+width, y+height, 1,1
        });
        //Воспользовались программой
        glUseProgram(textureShader);
        final int stride = (POSITION_COMPONENT_COUNT + UV_COMPONENT_COUNT)*BYTES_PER_FLOAT;
        //Указываем значения для вершин
        final int aPosition = glGetAttribLocation(textureShader, "a_Position");
        nativeVertexes.position(0);
        glVertexAttribPointer(
                aPosition,
                POSITION_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                stride,
                nativeVertexes
        );
        glEnableVertexAttribArray(aPosition);
        //Указываем значения для цвета
        final int aTextureCoordinates = glGetAttribLocation(textureShader, "a_TextureCoordinates");
        nativeVertexes.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(
                aTextureCoordinates,
                UV_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                stride,
                nativeVertexes
        );
        glEnableVertexAttribArray(aTextureCoordinates);
        //Применили матрицу
        useMatrix(textureShader);

        //Указали прозрачность
        final int uTransparency = glGetUniformLocation(textureShader, "u_Transparency");
        glUniform1f(uTransparency, opacity);

        //Указали текстуру
        final int uTextureUnit = glGetUniformLocation(textureShader, "u_TextureUnit");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, b);
        glUniform1i(uTextureUnit, 0);

        //Отрисовали
        glDrawArrays(GL_TRIANGLES, 0, 6);

        //Сбрасили текстуру
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, String text) {

    }

    @Override
    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {

    }

    @Override
    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        //Создаем массив вершин
        //Рисовать можно ТОЛЬКО треугольники, а поэтому в массив записаны вершины
        //Двух треугольников (но потом это выпиливается к херам и приделывется два массива
        //первый будет состоять из 4 float (вершин), а второй из 6 int (порядок обхода)
        final FloatBuffer nativeVertexes = createNativeFloatArray(new float[]{
                //Нижний левый угол
                x,y,
                //Верхний правый угол
                x2,y2,
                //Верхний левый угол
                x, y2,
                //Нижний левый угол
                x,y,
                //Нижний правый угол
                x2,y,
                //Верхний правый угол
                x2,y2
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
        useMatrix(fillColorShader);

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
        //GL_QUADS сокрыта куча кода, которой быть не должно
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

}
