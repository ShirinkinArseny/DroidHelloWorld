package live.wallpaper.Configs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import live.wallpaper.World;

import java.util.ArrayList;

public class Configs {

    private static ArrayList<ConfigField> fields;

    public static final int aiDeltaTarget=0;
    public static final int bloodDraw=1;
    public static final int aiOurUnitsCountToAttack=2;
    public static final int aiOurUnitsWithGiantCountToAttack=3;
    public static final int aiTheirUnitsCountToNotAttack=4;
    public static final int bloodVisibleTime=5;
    public static final int bloodCount=6;
    public static final int messageShowTime=7;
    public static final int messageDraw=8;
    public static final int spawnsShowTime=9;
    public static final int spawnsDraw=10;
    public static final int timerTimer=11;
    public static final int timerDraw=12;
    public static final int windDraw=13;
    public static final int worldGianSpawnProbability=14;
    public static final int worldTowerSpawnProbability=15;
    public static final int worldHorizontalBorders=16;
    public static final int worldVerticalTopBorders=17;
    public static final int worldVerticalBottomBorders=18;
    public static final int worldBoardersDraw=19;
    public static final int bloodInterval=20;

    private static SharedPreferences.OnSharedPreferenceChangeListener settingsListener;

    public static void init(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settingsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                for (ConfigField f : fields) {
                    switch (f.getType()) {
                        case Float:
                            f.setValue(Float.valueOf(sharedPreferences.getString(f.getName(), null)));
                            break;
                        case Integer:
                            f.setValue(Integer.valueOf(sharedPreferences.getString(f.getName(), null)));
                            break;
                        case String:
                            f.setValue(sharedPreferences.getString(f.getName(), null));
                            break;
                        case Boolean:
                            f.setValue(sharedPreferences.getBoolean(f.getName(), false));
                            break;
                    }
                }
                World.reInit();
            }
        };
        settings.registerOnSharedPreferenceChangeListener(settingsListener);
        fields=new ArrayList<>();
        fields.add(new IntegerField("aiDeltaTarget"));
        fields.add(new BooleanField("bloodDraw"));
        fields.add(new IntegerField("aiOurUnitsCountToAttack"));
        fields.add(new IntegerField("aiOurUnitsWithGiantCountToAttack"));
        fields.add(new IntegerField("aiTheirUnitsCountToNotAttack"));
        fields.add(new FloatField("bloodVisibleTime"));
        fields.add(new IntegerField("bloodCount"));
        fields.add(new FloatField("messageShowTime"));
        fields.add(new BooleanField("messageDraw"));
        fields.add(new FloatField("spawnsShowTime"));
        fields.add(new BooleanField("spawnsDraw"));
        fields.add(new IntegerField("timerTimer"));
        fields.add(new BooleanField("timerDraw"));
        fields.add(new BooleanField("windDraw"));
        fields.add(new IntegerField("worldGianSpawnProbability"));
        fields.add(new IntegerField("worldTowerSpawnProbability"));
        fields.add(new IntegerField("worldHorizontalBorders"));
        fields.add(new IntegerField("worldVerticalTopBorders"));
        fields.add(new IntegerField("worldVerticalBottomBorders"));
        fields.add(new BooleanField("worldBoardersDraw"));
        fields.add(new FloatField("bloodInterval"));
        settingsListener.onSharedPreferenceChanged(settings, null);
    }

    private static Object getValue(int num) {
        return fields.get(num).getValue();
    }

    public static Integer getIntValue(int num) {
        return (Integer)getValue(num);
    }

    public static Float getFloatValue(int num) {
        return (Float)getValue(num);
    }

    public static Boolean getBooleanValue(int num) {
        return (Boolean)getValue(num);
    }

    //TODO: убрать эту устаревшую парашу, ввести класс ColorField
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

    private static int[]     worldBoardersColor=new int[]{32, 32, 32, 128}; //Цвет границы

    private static int[] redFontColor=new int[]{253, 105, 67, 255};   // Цвет красного шрифта
    private static int[] blueFontColor=new int[]{176, 121, 182, 255};  // Цвет синего шрифта
    private static int[] grayFontColor=new int[]{222, 222, 222, 255};// Цвет шрифта таймера

    private static int displayWidth;    // Ширина экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ just make getter/setter
    private static int displayHeight;   // Высота экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ

}
