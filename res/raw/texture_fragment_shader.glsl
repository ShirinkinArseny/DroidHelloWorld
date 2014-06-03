precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform float u_Transparency;

void main()
{
    //Задаем текстурные координаты
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    //Задаем прозрачность
    gl_FragColor.a *= u_Transparency;
}