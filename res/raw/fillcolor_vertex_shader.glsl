//Позиция
attribute vec4 a_Position;
//Цвет
attribute vec4 a_Color;
varying vec4 v_Color;
//Матрица для преобразования в СК OpenGL
uniform mat4 u_Matrix;

void main()
{
    v_Color = a_Color;
    gl_Position = u_Matrix * a_Position;
}