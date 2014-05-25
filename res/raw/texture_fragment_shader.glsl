precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying float v_Transparency;

void main()
{
    //Задаем координатные текстуры
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    //Задаем прозрачность
    gl_FragColor.a *= v_Transparency;
}