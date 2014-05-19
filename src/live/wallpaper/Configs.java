package live.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class Configs {

    private static SharedPreferences settings;
    private static SharedPreferences.OnSharedPreferenceChangeListener settingsListener = null;

    private static int getInt(SharedPreferences preferences,String name)
    {
        Log.d("Configs", "Load " + name  + " value = " + preferences.getString(name, null));
        return Integer.parseInt(preferences.getString(name, null));
    }
    private static String getString(SharedPreferences preferences, String name)
    {
        Log.d("Configs", "Load " + name  + " value = " + preferences.getString(name, null));
        return preferences.getString(name, null);
    }
    private static boolean getBoolean(SharedPreferences preferences, String name)
    {
        Log.d("Configs", "Load " + name  + " value = " + preferences.getBoolean(name, false));
        return preferences.getBoolean(name, false);
    }
    private static float getFloat(SharedPreferences preferences, String name)
    {
        Log.d("Configs", "Load " + name  + " value = " + preferences.getString(name, null));
        return Float.parseFloat(preferences.getString(name, null));
    }




    public static void init(Context context)
    {
        //Получаем настройки
        //Если пользователь не поменял переменную в настройках - будет передано значение
        //android;default_value из .xml файла
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        settingsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("settingsListener", "Go to switch(key)");
                switch (key) {
                    case "aiDeltaTarget": setAiDeltaTarget(getInt(sharedPreferences, key)); break;
                    case "bloodDraw": setBloodDraw(getBoolean(sharedPreferences, key));     break;
                    case "aiOurUnitsCountToAttack": setAiDeltaTarget(getInt(sharedPreferences, key)); break;
                    case "aiOurUnitsWithGiantCountToAttack": setAiOurUnitsWithGiantCountToAttack(getInt(sharedPreferences, key)); break;
                    case "aiTheirUnitsCountToNotAttack": setAiTheirUnitsCountToNotAttack(getInt(sharedPreferences, key)); break;
                    case "bloodVisibleTime": setBloodVisibleTime(getFloat(sharedPreferences, key)); break;
                    case "bloodCount": setBloodCount(getInt(sharedPreferences, key)); break;
                    case "messageShowTime": setMessageShowTime(getFloat(sharedPreferences, key)); break;
                    case "messageDraw": setMessageDraw(getBoolean(sharedPreferences,key)); break;
                    case "spawnsShowTime": setSpawnsShowTime(getFloat(sharedPreferences, key)); break;
                    case "spawnsDraw": setSpawnsDraw(getBoolean(sharedPreferences, key)); break;
                    case "timerTimer": setTimerTimer(getInt(sharedPreferences, key)); break;
                    case "timerDraw": setTimerDraw(getBoolean(sharedPreferences, key)); break;
                    case "windDraw": setWindDraw(getBoolean(sharedPreferences, key)); break;
                    case "worldGianSpawnProbability": setWorldGianSpawnProbability(getInt(sharedPreferences, key)); break;
                    case "worldTowerSpawnProbability": setWorldTowerSpawnProbability(getInt(sharedPreferences, key)); break;
                    case "worldHorizontalBorders": setWorldHorizontalBorders(getInt(sharedPreferences, key)); break;
                    case "worldVerticalTopBorders": setWorldVerticalTopBorders(getInt(sharedPreferences, key)); break;
                    case "worldVerticalBottomBorders": setWorldVerticalBottomBorders(getInt(sharedPreferences, key)); break;
                    case "worldBoardersDraw": setWorldBoardersDraw(getBoolean(sharedPreferences, key)); break;
                    case "bloodInterval": setBloodInterval(getFloat(sharedPreferences, key)); break;
                }
            }
        };
        settings.registerOnSharedPreferenceChangeListener(settingsListener);
        //TODO: Здесь нужно присвоить всем значениям переменных значения из settings
        //Например вот так (для Integer)
        aiDeltaTarget = getInt(settings, "aiDeltaTarget");
        //Или вот так (для Boolean)
        bloodDraw = getBoolean(settings, "bloodDraw");
        aiOurUnitsCountToAttack = getInt(settings, "aiOurUnitsCountToAttack");
        aiOurUnitsWithGiantCountToAttack = getInt(settings, "aiOurUnitsWithGiantCountToAttack");
        aiTheirUnitsCountToNotAttack = getInt(settings, "aiTheirUnitsCountToNotAttack");
        bloodVisibleTime = getFloat(settings, "bloodVisibleTime");
        bloodCount = getInt(settings, "bloodCount");
        messageShowTime = getFloat(settings, "messageShowTime");
        messageDraw = getBoolean(settings, "messageDraw");
        spawnsShowTime = getFloat(settings, "spawnsShowTime");
        spawnsDraw = getBoolean(settings, "spawnsDraw");
        timerTimer = getInt(settings, "timerTimer");
        timerDraw = getBoolean(settings, "timerDraw");
        windDraw = getBoolean(settings, "windDraw");
        worldGianSpawnProbability = getInt(settings, "worldGianSpawnProbability");
        worldTowerSpawnProbability = getInt(settings, "worldTowerSpawnProbability");
        worldHorizontalBorders = getInt(settings, "worldHorizontalBorders");
        worldVerticalTopBorders = getInt(settings, "worldVerticalTopBorders");
        worldVerticalBottomBorders = getInt(settings, "worldVerticalBottomBorders");
        worldBoardersDraw = getBoolean(settings, "worldBoardersDraw");
        bloodInterval = getFloat(settings, "bloodInterval");

    }

    public static int getAiDeltaTarget() {
        return aiDeltaTarget;
    }

    public static void setAiDeltaTarget(int aiDeltaTarget) {
        Configs.aiDeltaTarget = aiDeltaTarget;
    }

    public static int getAiOurUnitsCountToAttack() {
        return aiOurUnitsCountToAttack;
    }

    public static void setAiOurUnitsCountToAttack(int aiOurUnitsCountToAttack) {
        Configs.aiOurUnitsCountToAttack = aiOurUnitsCountToAttack;
    }

    public static int getAiOurUnitsWithGiantCountToAttack() {
        return aiOurUnitsWithGiantCountToAttack;
    }

    public static void setAiOurUnitsWithGiantCountToAttack(int aiOurUnitsWithGiantCountToAttack) {
        Configs.aiOurUnitsWithGiantCountToAttack = aiOurUnitsWithGiantCountToAttack;
    }

    public static int getAiTheirUnitsCountToNotAttack() {
        return aiTheirUnitsCountToNotAttack;
    }

    public static void setAiTheirUnitsCountToNotAttack(int aiTheirUnitsCountToNotAttack) {
        Configs.aiTheirUnitsCountToNotAttack = aiTheirUnitsCountToNotAttack;
    }

    public static float getBloodVisibleTime() {
        return bloodVisibleTime;
    }

    public static void setBloodVisibleTime(float bloodVisibleTime) {
        Configs.bloodVisibleTime = bloodVisibleTime;
    }

    public static int getBloodCount() {
        return bloodCount;
    }

    public static void setBloodCount(int bloodCount) {
        Configs.bloodCount = bloodCount;
    }

    public static boolean isBloodDraw() {
        return bloodDraw;
    }

    public static void setBloodDraw(boolean bloodDraw) {
        Configs.bloodDraw = bloodDraw;
    }

    public static float getMessageShowTime() {
        return messageShowTime;
    }

    public static void setMessageShowTime(float messageShowTime) {
        Configs.messageShowTime = messageShowTime;
    }

    public static boolean isMessageDraw() {
        return messageDraw;
    }

    public static void setMessageDraw(boolean messageDraw) {
        Configs.messageDraw = messageDraw;
    }

    public static float getSpawnsShowTime() {
        return spawnsShowTime;
    }

    public static void setSpawnsShowTime(float spawnsShowTime) {
        Configs.spawnsShowTime = spawnsShowTime;
    }

    public static boolean isSpawnsDraw() {
        return spawnsDraw;
    }

    public static void setSpawnsDraw(boolean spawnsDraw) {
        Configs.spawnsDraw = spawnsDraw;
    }

    public static int getTimerTimer() {
        return timerTimer;
    }

    public static void setTimerTimer(int timerTimer) {
        Configs.timerTimer = timerTimer;
    }

    public static boolean isTimerDraw() {
        return timerDraw;
    }

    public static void setTimerDraw(boolean timerDraw) {
        Configs.timerDraw = timerDraw;
    }

    public static boolean isWindDraw() {
        return windDraw;
    }

    public static void setWindDraw(boolean windDraw) {
        Configs.windDraw = windDraw;
    }

    public static int getWorldGianSpawnProbability() {
        return worldGianSpawnProbability;
    }

    public static void setWorldGianSpawnProbability(int worldGianSpawnProbability) {
        Configs.worldGianSpawnProbability = worldGianSpawnProbability;
    }

    public static int getWorldTowerSpawnProbability() {
        return worldTowerSpawnProbability;
    }

    public static void setWorldTowerSpawnProbability(int worldTowerSpawnProbability) {
        Configs.worldTowerSpawnProbability = worldTowerSpawnProbability;
    }

    public static int getWorldHorizontalBorders() {
        return worldHorizontalBorders;
    }

    public static void setWorldHorizontalBorders(int worldHorizontalBorders) {
        Configs.worldHorizontalBorders = worldHorizontalBorders;
    }

    public static int getWorldVerticalTopBorders() {
        return worldVerticalTopBorders;
    }

    public static void setWorldVerticalTopBorders(int worldVerticalTopBorders) {
        Configs.worldVerticalTopBorders = worldVerticalTopBorders;
    }

    public static int getWorldVerticalBottomBorders() {
        return worldVerticalBottomBorders;
    }

    public static void setWorldVerticalBottomBorders(int worldVerticalBottomBorders) {
        Configs.worldVerticalBottomBorders = worldVerticalBottomBorders;
    }

    public static boolean isWorldBoardersDraw() {
        return worldBoardersDraw;
    }

    public static void setWorldBoardersDraw(boolean worldBoardersDraw) {
        Configs.worldBoardersDraw = worldBoardersDraw;
    }

    public static int[] getWorldBoardersColor() {
        return worldBoardersColor;
    }

    public static void setWorldBoardersColor(int[] worldBoardersColor) {
        Configs.worldBoardersColor = worldBoardersColor;
    }

    public static int[] getRedFontColor() {
        return redFontColor;
    }

    public static void setRedFontColor(int[] redFontColor) {
        Configs.redFontColor = redFontColor;
    }

    public static int[] getBlueFontColor() {
        return blueFontColor;
    }

    public static void setBlueFontColor(int[] blueFontColor) {
        Configs.blueFontColor = blueFontColor;
    }

    public static int[] getGrayFontColor() {
        return grayFontColor;
    }

    public static void setGrayFontColor(int[] grayFontColor) {
        Configs.grayFontColor = grayFontColor;
    }

    public static int getDisplayWidth() {
        return displayWidth;
    }

    public static void setDisplayWidth(int displayWidth) {
        Configs.displayWidth = displayWidth;
    }

    public static int getDisplayHeight() {
        return displayHeight;
    }

    public static void setDisplayHeight(int displayHeight) {
        Configs.displayHeight = displayHeight;
    }

    public static void setBloodInterval(float bloodInterval) {Configs.bloodInterval = bloodInterval; }

    public static float getBloodInterval() {
        return bloodInterval;
    }

    private static int       aiDeltaTarget=100;//Разброс толпы юнитов при атаке
    private static int       aiOurUnitsCountToAttack=40;//Кол-во юнитов, при котором можно начинать нападать на их башни
    private static int       aiOurUnitsWithGiantCountToAttack=20;//Кол-во юнитов (если есть гигант) для атаки башен
    private static int       aiTheirUnitsCountToNotAttack=50;//Кол-во вражеских юнитов, при котором игнорим предыдущие команды и начинаем выпиливать массово их

    private static float     bloodVisibleTime =0.1f;
    private static float     bloodInterval =0.1f;
    private static int       bloodCount =10;//Количество крови
    private static boolean   bloodDraw =true;//Отрисовывать ли крось

    private static float     messageShowTime =0.5f;//Скорость сокрытия сообщений
    private static boolean   messageDraw =true;//Отрисовывать ли сообщения

    private static float     spawnsShowTime =1f; //Скорость сокрытия мест спавне
    private static boolean   spawnsDraw=true; //Отрисовывать ли места спавне

    private static int       timerTimer =60*5; //Время раунда в сек
    private static boolean   timerDraw =true; //Отрисовывать ли таймер

    private static boolean   windDraw=false;

    private static int       worldGianSpawnProbability =100; //Вероятность спавна гиганта при спавне юнита
    private static int       worldTowerSpawnProbability =80; //Вероятность спавна башни при спавне юнита
    private static int       worldHorizontalBorders=15;     //Боковые границы области спавна
    private static int       worldVerticalTopBorders=40;    //Верхняя граница области спавна
    private static int       worldVerticalBottomBorders=15;  //Нижняя граница области спавна
    private static boolean   worldBoardersDraw=true;         //Отрисовывать ли границу
    private static int[]     worldBoardersColor=new int[]{32, 32, 32, 128}; //Цвет границы

    private static int[] redFontColor=new int[]{192, 64, 64, 128};   // Цвет красного шрифта
    private static int[] blueFontColor=new int[]{64, 64, 192, 128};  // Цвет синего шрифта
    private static int[] grayFontColor=new int[]{128, 128, 128, 128};// Цвет шрифта таймера

    private static int displayWidth;    // Ширина экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ just make getter/setter
    private static int displayHeight;   // Высота экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ

}
