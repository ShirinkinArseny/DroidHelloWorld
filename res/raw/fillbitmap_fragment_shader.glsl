precision mediump float;

//Текстурные координаты
varying vec2 v_TexturePosition;
//Текстура
uniform sampler2D u_TextureUnit;

//Размеры текстуры
uniform vec2 u_TextureDimensions;
//Смещение по dx
uniform float u_DX;


uniform float u_Opacity;

void main() {
    vec2 rawPosition = v_TexturePosition;
    rawPosition.x -= u_DX;
    vec2 colorPosition = rawPosition / u_TextureDimensions;
    vec4 color = texture2D(u_TextureUnit, colorPosition);
    color.a *= u_Opacity;
    gl_FragColor = color;
}