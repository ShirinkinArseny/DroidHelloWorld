package live.wallpaper.OpenGLIntegration;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import live.wallpaper.Configs.IntegerField;
import live.wallpaper.Configs.LoggerConfig;
import live.wallpaper.OpenGLIntegration.Shaders.FillColorShader;
import live.wallpaper.OpenGLIntegration.Shaders.Generators.ShaderGenerator;
import live.wallpaper.OpenGLIntegration.Shaders.Generators.TextureGenerator;
import live.wallpaper.OpenGLIntegration.Shaders.Shader;
import live.wallpaper.OpenGLIntegration.Shaders.TextureShader;
import live.wallpaper.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;


import static android.opengl.GLES20.*;

public class OpenGLES20Engine {

    private static final String TAG = "OpenGLES20Engine";

    //Создание матрицы преобразования для вывода на экран с использованием абсолютной системы координат
    //Собственно матрица (матрица проекции)
    private static FloatBuffer projectionMatrix;

    /**
     * Включает использование матрицы преобразования в шейдере
     * @param shader Программа шейдера
     */
    private static void useMatrix(Shader shader) {
        //Задаем матрицу для того чтобы отойти от экранной СК
        projectionMatrix.position(0);
        shader.setMatrix(projectionMatrix);
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
    private static FloatBuffer createFloatBuffer(int length) {
        return  ByteBuffer.
                //Теперь задаем размер таким, чтобы влезли все величины
                        allocateDirect(length * BYTES_PER_FLOAT).
                //Задаем порядок, родной для машины (прямой (0x01) или перевернутый (0x10))
                        order(ByteOrder.nativeOrder()).
                //Храним его как массив значений float
                        asFloatBuffer();
    }
    private static void putValuesIntoFloatBuffer(float[] values, FloatBuffer floatBuffer) {
        floatBuffer.clear();
        floatBuffer.put(values);
    }

    //Шейдер, который просто заливает всё цветом
    private static FillColorShader fillColorShader;
    //Шейдер, который заливает всё текстурой и прозрачностью
    private static TextureShader textureShader;

    //Итентификатор VBO
    private static int vboId;

    /**
     * Инициализация системы. Компилирование всего и вся.
     * @param context Контекст, в котором содержатся все ресурсы
     */
    public static void init(Context context) {
        //Загружаем шейдеры
        fillColorShader = new FillColorShader(context);
        fillColorShader.validate();
        textureShader = new TextureShader(context);
        textureShader.validate();

        //Создаем VBO для вершин прямоугольника
        int[] buffers = new int[1];
        glGenBuffers(1,buffers,0);
        if (buffers[0]==0)
            LoggerConfig.e(TAG, "Could not create buffers");
        vboId = buffers[0];
        //Создаем вершины
        FloatBuffer vboBufferVertexes = createNativeFloatArray(new float[]{
            //Левый нижний угол
                0,0,
            //Правый верхний угол
                1,1,
            //Левый верхний угол
                0,1,
            //Правый нижний угол
                1,0,
            //Левый нижний угол
                0,0,
            //Правый верхний угол
                1,1
        });
        //Задаем указатель на начало массива
        vboBufferVertexes.position(0);
        //Указываем данные
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER,vboBufferVertexes.capacity() * BYTES_PER_FLOAT, vboBufferVertexes, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        //Включаем alpha-blending
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
    }

    /**
     * Уничтожение контекста
     */
    public static void destroy() {
        fillColorShader.delete();
        textureShader.delete();
        int i=0;
        int[] goodArray = new int[textures.size()];
        for (Integer texture : textures)
            goodArray[i++] = texture;
        glDeleteTextures(goodArray.length, goodArray, 0);
    }


    enum Mode {
        DRAW_RECTANGLES,
        DRAW_BITMAPS
    }

    Mode currentMode;

    /**
     * Задаем режим отрисовки прямоугольников
     */
    private static void initRectangles() {
        //Задаем буфер для отрисовки
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        //Задаем шейдер для отрисовки
        fillColorShader.use();
        //Определяем местонахождение всех атрибутов
        final int aPosition = fillColorShader.get_aPosition();
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);
    }

    /**
     * Задаем режим отрисовки текстур
     */
    private static void initBitmaps() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        textureShader.use();
        final int aPosition = textureShader.get_aPosition();
        final int aTexturePosition = textureShader.get_aTextureCoordinates();
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);
        //Для текстурных координат и для позиции используем одни и те же значения
        glEnableVertexAttribArray(aTexturePosition);
        glVertexAttribPointer(aTexturePosition, UV_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);

        //Задаем 0-й юнит для текстуры
        //Так как текстура всего одна -- то всё будет в порядке
        //Если текстур будет больше -- изменить легко
        //Для этого во время отрисовки нужно вызывать glActiveTexture(GL_NUM)
        //И потом glBindTexture()
        glActiveTexture(GL_TEXTURE0);
        textureShader.setTexture(0);
    }


    /**
     * Начало отрисовки. Очищение экрана белым цветом.
     */
    public void startDraw(Mode drawMode) {
        currentMode = drawMode;
        switch (drawMode) {
            case DRAW_RECTANGLES: initRectangles();
                break;
            case DRAW_BITMAPS:    initBitmaps();
                break;
        }
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void finishDraw() {

    }


    //Список для хранения текстур чтобы их потом удалить
    private static LinkedList<Integer> textures = new LinkedList<>();

    /**
     * Генерация текстуры из Bitmap
     * @param b Битмап, из которого создается текстура (величина сторон должна быть степенью двойки!)
     * @return Итентификатор
     */
    public int genTexture(Bitmap b) {
        final int id = TextureGenerator.loadTexture(b);
        textures.add(id);
        return id;
    }

    public void drawBitmap(int b, float x, float y, float x1, float y1) {
        drawBitmap(b,x,y,x1,y1,1.0f);
    }

    private static final FloatBuffer drawBitmapVertexes = createFloatBuffer(6*(POSITION_COMPONENT_COUNT+UV_COMPONENT_COUNT));

    public void drawBitmap(int b, float x, float y, float x1, float y1, float opacity) {

        //Вершины со всеми значениями
        putValuesIntoFloatBuffer(new float[]{
                //Порядок: вершины, текстурные координаты
                //Левый нижний угол
                x,y, 0,0,
                //Правый верхний угол
                x1, y1, 1,1,
                //Левый верхний угол
                x,y1, 0,1,
                //Правый нижний угол
                x1, y, 1,0,
                //Левый нижний угол
                x,y, 0,0,
                //Правый верхний угол
                x1, y1, 1,1
        }, drawBitmapVertexes);
        //Воспользовались программой
        textureShader.use();
        final int stride = (POSITION_COMPONENT_COUNT + UV_COMPONENT_COUNT)*BYTES_PER_FLOAT;
        //Указываем значения для вершин
        final int aPosition = textureShader.get_aPosition();
        drawBitmapVertexes.position(0);
        glVertexAttribPointer(
                aPosition,
                POSITION_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                stride,
                drawBitmapVertexes
        );
        glEnableVertexAttribArray(aPosition);
        //Указываем значения для цвета
        final int aTextureCoordinates = textureShader.get_aTextureCoordinates();
        drawBitmapVertexes.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(
                aTextureCoordinates,
                UV_COMPONENT_COUNT,
                GL_FLOAT,
                false,
                stride,
                drawBitmapVertexes
        );
        glEnableVertexAttribArray(aTextureCoordinates);
        //Применили матрицу
        useMatrix(textureShader);

        //Указали прозрачность
        textureShader.setTransparency(opacity);

        //Указали текстуру
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, b);
        textureShader.setTexture(0);

        //Отрисовали
        glDrawArrays(GL_TRIANGLES, 0, 6);

        //Сбрасили текстуру
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void drawText(float x, float y, float size, float r, float g, float b, String text) {

    }

    public void drawText(float x, float y, float size, float r, float g, float b, float a, String text) {

    }

    private static final FloatBuffer drawRectVertexes = createFloatBuffer(6*POSITION_COMPONENT_COUNT);

    public void drawRect(float x, float y, float x2, float y2, float r, float g, float b, float a) {
        //Создаем массив вершин
        //Рисовать можно ТОЛЬКО треугольники, а поэтому в массив записаны вершины
        //Двух треугольников (но потом это выпиливается к херам и приделывется два массива
        //первый будет состоять из 4 float (вершин), а второй из 6 int (порядок обхода)
        putValuesIntoFloatBuffer(new float[]{
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
                x2,y2},drawRectVertexes );
        //Используем программу простой заливки
        fillColorShader.use();
        final int aPosition = fillColorShader.get_aPosition();
        //Устанавливаем указатель на начало массива
        drawRectVertexes.position(0);
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
                drawRectVertexes);
        //Заюзали атрибут
        glEnableVertexAttribArray(aPosition);
        useMatrix(fillColorShader);

        //Задаем цвет
        fillColorShader.setColor(r,g,b,a);


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
