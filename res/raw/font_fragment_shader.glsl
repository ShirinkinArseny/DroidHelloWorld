precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform vec4 u_Color;

//Для получения символа в карте
//Размеры одного символа (от 0 до 1)
uniform vec2 u_SymbolDimensions;
//Положение данной буквы в карте
uniform vec2 u_CharPosition;

void main()
{
    vec2 TextureCoordinates = v_TextureCoordinates;
    //Определяем текстурные координаты
    //Определяем смещение в глобальной координатной системе
    TextureCoordinates *= u_SymbolDimensions;
    TextureCoordinates += u_CharPosition * u_SymbolDimensions;
    //Получаем цвет из текстуры
    gl_FragColor = texture2D(u_TextureUnit, TextureCoordinates);
    //Задаем цвет
    gl_FragColor *= u_Color;
}