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
        Log.i("Configs", "Loaded");
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
    public static float[] getWorldBoardersColor() {
        return worldBoardersColor;
    }

    public static float[] getWorldBGColor() {
        return worldBGColor;
    }

    public static void setWorldBoardersColor(float [] worldBoardersColor) {
        Configs.worldBoardersColor = worldBoardersColor;
    }

    public static float[] getRedFontColor() {
        return redFontColor;
    }

    public static void setRedFontColor(float[] redFontColor) {
        Configs.redFontColor = redFontColor;
    }

    public static float[] getBlueFontColor() {
        return blueFontColor;
    }

    public static void setBlueFontColor(float[] blueFontColor) {
        Configs.blueFontColor = blueFontColor;
    }

    public static float[] getGrayFontColor() {
        return grayFontColor;
    }

    public static void setGrayFontColor(float [] grayFontColor) {
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

    private static float[]     worldBoardersColor=new float[]{0.6156863f, 0.65882355f, 0.5647059f, 1}; //Цвет границы
    private static final float[]     worldBGColor=new float[]{0.54901963f, 0.5803922f, 0.4862745f, 1}; //Цвет фона

    private static float[] redFontColor=new float[]{0.99215686f, 0.4117647f, 0.2627451f, 1.0f};   // Цвет красного шрифта
    private static float[] blueFontColor=new float[]{0.6901961f, 0.4745098f, 0.7137255f, 1.0f};  // Цвет синего шрифта
    private static float[] grayFontColor=new float[]{0.87058824f, 0.87058824f, 0.87058824f, 1.0f};// Цвет шрифта таймера

    private static int displayWidth;    // Ширина экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ just make getter/setter
    private static int displayHeight;   // Высота экрана !!НЕ ДОБАВЛЯТЬ В МЕНЮ

}
