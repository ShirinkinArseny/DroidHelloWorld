package live.wallpaper;

public class Configs {

    public static int       aiDeltaTarget=100;//Разброс толпы юнитов при атаке
    public static int       aiOurUnitsCountToAttack=40;//Кол-во юнитов, при котором можно начинать нападать на их башни
    public static int       aiOurUnitsWithGiantCountToAttack=20;//Кол-во юнитов (если есть гигант) для атаки башен
    public static int       aiTheirUnitsCountToNotAttack=50;//Кол-во вражеских юнитов, при котором игнорим предыдущие команды и начинаем выпиливать массово их

    public static float     bloodHideCoef =5;//Скорость сокрытия крови (трещины - тоже кровь, только машинное масло)
    public static int       bloodCount =10;//Количество крови
    public static boolean   bloodDraw =true;//Отрисовывать ли крось

    public static float     messageHideCoef =1.5f;//Скорость сокрытия сообщений
    public static boolean   messageDraw =true;//Отрисовывать ли сообщения

    public static float     spawnsHideCoef =1f; //Скорость сокрытия мест спавне
    public static boolean   spawnsDraw=true; //Отрисовывать ли места спавне

    public static int       timerTimer =60*5; //Время раунда в сек
    public static boolean   timerDraw =true; //Отрисовывать ли таймер

    public static boolean   windDraw=false;

    public static int       worldGianSpawnProbability =100; //Вероятность спавна гиганта при спавне юнита
    public static int       worldTowerSpawnProbability =80; //Вероятность спавна башни при спавне юнита
    public static int       worldHorizontalBorders=15;     //Боковые границы области спавна
    public static int       worldVerticalTopBorders=40;    //Верхняя граница области спавна
    public static int       worldVerticalBottomBorders=15;  //Нижняя граница области спавна
    public static boolean   worldBoardersDraw=true;         //Отрисовывать ли границу
    public static int[]     worldBoardersColor=new int[]{32, 32, 32, 128}; //Цвет границы

    public static int[] redFontColor=new int[]{192, 64, 64, 128};   // Цвет красного шрифта
    public static int[] blueFontColor=new int[]{64, 64, 192, 128};  // Цвет синего шрифта
    public static int[] grayFontColor=new int[]{128, 128, 128, 128};// Цвет шрифта таймера

    public static int displayWidth;    // Ширина экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ just make getter/setter
    public static int displayHeight;   // Высота экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ

}
