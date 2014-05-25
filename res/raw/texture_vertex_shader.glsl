//Позиция
attribute vec4 a_Position;
//Позиция в текстуре
attribute vec2 a_TextureCoordinates;
//Для передачи фрагментному шейдеру
varying vec2 v_TextureCoordinates;

attribute float a_Transparency;
varying float v_Transparency;

//Матрица для преобразования в СК OpenGL
uniform mat4 u_Matrix;

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    v_Transparency = a_Transparency;
    gl_Position = u_Matrix * a_Position;
}