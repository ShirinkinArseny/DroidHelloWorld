precision mediump float;

//Текстурные координаты
varying vec2 v_TexturePosition;
//Текстура
uniform sampler2D u_TextureUnit;

//Размеры текстуры
uniform vec2 u_TextureDimensions;
//Смещение по dx
uniform float u_DX;

void main() {
    vec2 colorPosition = mod(v_TexturePosition-vec2(u_DX,0), u_TextureDimensions);
    colorPosition.x /= u_TextureDimensions.x;
    colorPosition.y /= u_TextureDimensions.y;
    gl_FragColor = texture2D(u_TextureUnit, colorPosition);
}