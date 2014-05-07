package live.wallpaper;

public class Configs {

    public static int       aiDeltaTarget=100;
    public static int       aiOurUnitsCountToAttack=40;
    public static int       aiOurUnitsWithGiantCountToAttack=20;
    public static int       aiTheirUnitsCountToNotAttack=50;

    public static float     bloodHideCoef =5;
    public static int       bloodCount =10;
    public static boolean   bloodDraw =true;

    public static float     messageHideCoef =1.5f;
    public static boolean   messageDraw =true;

    public static float     spawnsHideCoef =1f;
    public static boolean   spawnsDraw=true;

    public static int       timerTimer =60;//60*5;
    public static boolean   timerDraw =true;

    public static int       worldGianSpawnProbability =100;
    public static int       worldTowerSpawnProbability =80;
    public static int       worldBorders=25;
    public static boolean   worldBoardersDraw=true;

    public static int[] redFontColor=new int[]{255, 128, 128, 128};
    public static int[] blueFontColor=new int[]{128, 128, 255, 128};
    public static int[] grayFontColor=new int[]{128, 128, 128, 128};

    public static int displayWidth;
    public static int displayHeight;

}
