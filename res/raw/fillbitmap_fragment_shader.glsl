//Текстурные координаты
varying vec2 v_TexturePosition;
//Текстура
uniform sampler2D u_TextureUnit;

//Размеры текстуры
uniform vec2 u_TextureDimensions;
//Смещение по dx
uniform float u_DX;

void main() {
    gl_FragColor = texture2D(u_TextureUnit, mod(v_TexturePosition-vec2(u_DX, 0), u_TextureDimensions));
}