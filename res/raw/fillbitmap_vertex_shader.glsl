//Позиция (матрицы нинужны)
attribute vec4 a_Position;
varying vec2 v_TexturePosition;
void main()
{
    gl_Position = a_Position;
    //Так как заливаем весь экран -- то можно поступить по-хитрому
    //и задать текстурные координаты, исходя из вершинных координат.
    //Что в общем случае не правильно
    v_TexturePosition = (vec2(a_Position) + vec2(1.0,1.0))*0.5;
}