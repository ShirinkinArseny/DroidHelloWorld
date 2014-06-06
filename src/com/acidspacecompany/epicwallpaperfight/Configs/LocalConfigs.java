package com.acidspacecompany.epicwallpaperfight.Configs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.acidspacecompany.epicwallpaperfight.World;

import java.util.ArrayList;

public class LocalConfigs {
    private static final String TAG = "LocalConfigs";


    private static boolean isListening = true;
    public static void setListening(boolean value) {
        isListening = value;
    }

    private static ArrayList<ConfigField> fields;

    private static int lastNumber=-1;

    private static int getNum() {
        lastNumber++;
        return lastNumber;
    }

    public static final int bloodDraw=getNum();
    public static final int bloodVisibleTime=getNum();
    public static final int bloodCount=getNum();
    public static final int messageShowTime=getNum();
    public static final int messageDraw=getNum();
    public static final int spawnsShowTime=getNum();
    public static final int spawnsDraw=getNum();
    public static final int timerTimer=getNum();
    public static final int timerDraw=getNum();
    public static final int worldGianSpawnProbability=getNum();
    public static final int worldTowerSpawnProbability=getNum();
    public static final int worldHorizontalBorders=getNum();
    public static final int worldVerticalTopBorders=getNum();
    public static final int worldVerticalBottomBorders=getNum();

    private static SharedPreferences.OnSharedPreferenceChangeListener settingsListener;

    public static void init(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settingsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isListening) {
                    for (ConfigField f : fields) {
                        switch (f.getType()) {
                            case Float:
                                f.setValue(sharedPreferences.getFloat(f.getName(), -1));
                                break;
                            case Integer:
                                f.setValue(sharedPreferences.getInt(f.getName(), -1));
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
            }
        };
        settings.registerOnSharedPreferenceChangeListener(settingsListener);
        fields=new ArrayList<>();
        fields.add(new BooleanField("bloodDraw"));
        fields.add(new FloatField("bloodVisibleTime"));
        fields.add(new IntegerField("bloodCount"));
        fields.add(new FloatField("messageShowTime"));
        fields.add(new BooleanField("messageDraw"));
        fields.add(new FloatField("spawnsShowTime"));
        fields.add(new BooleanField("spawnsDraw"));
        fields.add(new IntegerField("timerTimer"));
        fields.add(new BooleanField("timerDraw"));
        fields.add(new IntegerField("worldGianSpawnProbability"));
        fields.add(new IntegerField("worldTowerSpawnProbability"));
        fields.add(new FloatField("worldHorizontalBorders"));
        fields.add(new FloatField("worldVerticalTopBorders"));
        fields.add(new FloatField("worldVerticalBottomBorders"));
        settingsListener.onSharedPreferenceChanged(settings, null);
        BicycleDebugger.i("Configs", "Loaded successfully");
    }

    public static ArrayList<ConfigField> getFields() {
        ArrayList<ConfigField> configs=new ArrayList<>();
        for (ConfigField f: fields) configs.add(f.copy());
        return configs;
    }

    public static void setFields(ArrayList<ConfigField> fields) {
        LocalConfigs.fields=fields;
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

    public static float[] getWorldBoardersColor() {
        return worldBoardersColor;
    }

    public static float[] getWorldBGColor() {
        return worldBGColor;
    }

    public static float[] getRedFontColor() {
        return redFontColor;
    }

    public static float[] getBlueFontColor() {
        return blueFontColor;
    }

    public static float[] getGrayFontColor() {
        return grayFontColor;
    }

    public static int getDisplayWidth() {
        return displayWidth;
    }

    public static int getDisplayHeight() {
        return displayHeight;
    }

    public static void resize(int w, int h) {
        LocalConfigs.displayWidth = w;
        LocalConfigs.displayHeight = h;
    }

    private static float[]     worldBoardersColor=new float[]{0.6156863f, 0.65882355f, 0.5647059f, 1}; //Цвет границы
    private static final float[]     worldBGColor=new float[]{0.54901963f, 0.5803922f, 0.4862745f, 1}; //Цвет фона

    private static float[] redFontColor=new float[]{0.99215686f, 0.4117647f, 0.2627451f, 1.0f};   // Цвет красного шрифта
    private static float[] blueFontColor=new float[]{0x48/255f, 0xb0/255f, 1.0f, 1.0f};  // Цвет синего шрифта
    private static float[] grayFontColor=new float[]{0.87058824f, 0.87058824f, 0.87058824f, 1.0f};// Цвет шрифта таймера

    private static int displayWidth;
    private static int displayHeight;

}