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
import java.util.concurrent.LinkedBlockingQueue;


import static android.opengl.GLES20.*;

public class Graphic {

    //Хранение размеров Bitmap по его ключу-итентификатору
    //TODO: выпилить нафиг
    private static Map textureDimensions;

    //Создание матрицы преобразования для вывода на экран с использованием абсолютной системы координат
    //Собственно матрица (матрица проекции)
    private static FloatBuffer projectionMatrix;

    //Очередь из значений
    private static Queue<Float> drawRectValues = new LinkedBlockingQueue<>();
    private static Map<Integer, Queue<Float>>drawBitmap = new LinkedHashMap<>();
    private static void addFloatArrayToQueue(float[] array, Queue<Float> queue) {
        for (int i=0; i<array.length; i++)
            queue.add(array[i]);
    }
    private static void addRect(float[] values) {
        addFloatArrayToQueue(values, drawRectValues);
    }
    private static void addDrawBitmap(float[] values, int textureId) {
        Queue<Float> queue;
        if ((queue = drawBitmap.get(textureId))!=null)
            addFloatArrayToQueue(values, queue);
        else
        {
            queue = new LinkedBlockingQueue<>();
            addFloatArrayToQueue(values, queue);
            drawBitmap.put(textureId, queue);
        }
    }

    private static FloatBuffer createBuffer(int length) {
        return ByteBuffer.allocateDirect(length).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
    private static void addQueueToBuffer(FloatBuffer buffer, Queue<Float> values) {
        Float value;
        while ((value = values.peek())!=null) {
            values.poll();
            buffer.put(value);
        }
    }

    private static FloatBuffer createNativeArrayFromQueue(Queue<Float> values) {
        FloatBuffer result = ByteBuffer.allocateDirect(values.size()*BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        addQueueToBuffer(result, values);
        return result;
    }

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
    //У цвета 4 компоненты
    private static final int COLOR_COMPONENT_COUNT = 4;

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
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
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
    public static void startDraw() {
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public enum DrawType {
        RECTANGLES,
        BITMAPS,
        SCALED_ALPHA_BITMAP
    }
    private static DrawType currentDrawType;

    public static void glBegin(DrawType drawType) {
        currentDrawType = drawType;
        switch (drawType) {
            case RECTANGLES:
                drawRectValues.clear();
                break;
            case BITMAPS:
                drawBitmap.clear();
                break;
            case SCALED_ALPHA_BITMAP:
                drawBitmap.clear();
                break;
        }
    }
    public static void glEnd() {
        switch (currentDrawType) {
            case RECTANGLES:
                drawAllRects();
                break;
            case BITMAPS:
                drawAllBitmaps();
                break;
            case SCALED_ALPHA_BITMAP:
                drawAllBitmaps();
                break;
        }
    }

    private static void drawAllRects(){
        FloatBuffer vertexes = createNativeArrayFromQueue(drawRectValues);
        //Внутри вершины записаны в следующем порядке:
        //Две координаты, четыре компоненты цвета
        final int componentCount = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT;

        //Получаем число вершин по числу значений float
        final int vertexesCount = drawRectValues.size() / componentCount;

        //Используем программу, которая зальет прямоугольник цветом
        glUseProgram(fillColorShader);
        //Позиция
        final int aPosition = glGetAttribLocation(fillColorShader, "a_Position");

        //Цвет
        final int aColor = glGetAttribLocation(fillColorShader, "a_Color");

        final int stride = componentCount * BYTES_PER_FLOAT;

        vertexes.position(0);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexes);
        glEnableVertexAttribArray(aPosition);

        vertexes.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColor, COLOR_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexes);
        glEnableVertexAttribArray(aColor);

        useMatrix(fillColorShader);

        glDrawArrays(GL_TRIANGLES, 0, vertexesCount);

        glUseProgram(0);
    }

    private static void drawAllBitmaps() {
        glUseProgram(textureShader);
        useMatrix(textureShader);

        //Создаем массив
        //Получаем все значения. Мы получим их в том порядке, в котором они существовали
        int[] keys = convert(drawBitmap.keySet().toArray());

        int[] lengths = new int[keys.length];

        int totalLength = 0;
        //Получаем общую длину всех вершин
        for(Queue<Float> queue : drawBitmap.values())
            totalLength += queue.size();
        //Создаем FloatBuffer такой длины
        FloatBuffer vertexes = createBuffer(totalLength * BYTES_PER_FLOAT);
        //Добавляем туда значения из всех очередей
        for (int i=0; i<keys.length; i++) {
            Queue<Float> queue = drawBitmap.get(keys[i]);
            lengths[i] = queue.size();
            addQueueToBuffer(vertexes, queue);
        }

        //Внутри значения идут в следующем порядке
        //Две координаты, две текстурные координаты, одно значение прозрачности
        final int aPosition = glGetAttribLocation(textureShader, "a_Position");
        final int aTextureCoordinates = glGetAttribLocation(textureShader, "a_TextureCoordinates");
        final int aTransparency = glGetAttribLocation(textureShader, "a_Transparency");


        //Получаем число компонент
        final int componentsByVertex = POSITION_COMPONENT_COUNT + UV_COMPONENT_COUNT + 1;
        final int stride = componentsByVertex * BYTES_PER_FLOAT;

        vertexes.position(0);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexes);
        glEnableVertexAttribArray(aPosition);

        vertexes.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aTextureCoordinates, UV_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexes);
        glEnableVertexAttribArray(aTextureCoordinates);

        vertexes.position(POSITION_COMPONENT_COUNT+1);
        glVertexAttribPointer(aTransparency, 1, GL_FLOAT, false, stride, vertexes);
        glEnableVertexAttribArray(aTransparency);

        //Рисуем по очереди все треугольники
        int position=0;
        //Для рисования будем использовать 0й samplerUnit
        glActiveTexture(GL_TEXTURE0);
        //Положение текстуры
        final int uTextureUnit = glGetUniformLocation(textureShader, "u_TextureUnit");
        for (int i=0; i<lengths.length; i++)
        {
            //Получаем число вершин в текущем массиве
            final int vertexNo = lengths[i]/componentsByVertex;
            //Задали текстуру по её итентификатору в keys
            glBindTexture(GL_TEXTURE_2D, keys[i]);
            glUniform1i(uTextureUnit, 0);

            glDrawArrays(GL_TRIANGLES, position, vertexNo);
            //Получили следующую позицию
            position+=vertexNo;
        }
        glBindTexture(GL_TEXTURE_2D, 0);
        glUseProgram(0);
    }

    public static void finishDraw() {

    }

    /**
     * Генерация текстуры из Bitmap
     * @param b Битмап, из которого создается текстура (величина сторон должна быть степенью двойки!)
     * @return Итентификатор
     */
    public static int genTexture(Bitmap b) {
        //LoggerConfig.d("OpenGLEngine", b.isRecycled()+"");
        final int id = TextureGenerator.loadTexture(b);
        textureDimensions.put(id, new float[]{b.getWidth(), b.getHeight()});
        return id;
    }

    public static void drawBitmap(int b, float x, float y) {
        drawBitmap(b,x,y,1.0f);
    }

    public static void drawBitmap(int b, float x, float y, float opacity) {

        //Получаем высоту
        final float[] dimensions = (float[])textureDimensions.get(b);
        final float width = dimensions[0];
        final float height = dimensions[1];

        //Вершины со всеми значениями
        final float[] nativeVertexes = new float[]{
                //Порядок: вершины, текстурные координаты
                //Левый нижний угол
                x,y, 0,0, opacity,
                //Правый верхний угол
                x+width, y+height, 1,1,  opacity,
                //Левый верхний угол
                x,y+height, 0,1,  opacity,
                //Правый нижний угол
                x+width, y, 1,0, opacity,
                //Левый нижний угол
                x,y, 0,0,  opacity,
                //Правый верхний угол
                x+width, y+height, 1,1, opacity
        };
        addDrawBitmap(nativeVertexes, b);
    }

    public static void drawText(float x, float y, float size, float r, float g, float b, String text) {

    }

    public static void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {

    }

    public static void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        //Создаем массив вершин
        //Рисовать можно ТОЛЬКО треугольники, а поэтому в массив записаны вершины
        //Двух треугольников (но потом это выпиливается к херам и приделывется два массива
        //первый будет состоять из 4 float (вершин), а второй из 6 int (порядок обхода)
        final float[] vertexes = new float[]{
                //Нижний левый угол
                x,y,r,g,b,a,
                //Верхний правый угол
                x2,y2,r,g,b,a,
                //Верхний левый угол
                x, y2,r,g,b,a,
                //Нижний левый угол
                x,y,r,g,b,a,
                //Нижний правый угол
                x2,y,r,g,b,a,
                //Верхний правый угол
                x2,y2,r,g,b,a
        };
        addRect(vertexes);
    }

}
