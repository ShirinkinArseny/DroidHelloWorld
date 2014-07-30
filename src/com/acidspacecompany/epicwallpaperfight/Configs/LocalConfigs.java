package com.acidspacecompany.epicwallpaperfight.Configs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.R;
import com.acidspacecompany.epicwallpaperfight.World;

import java.io.File;
import java.util.ArrayList;

public class LocalConfigs {

    public static final String PACKAGE_NAME = "com.acidspacecompany.epicwallpaperfight";
    public static final String BACKGROUND_FILE_NAME = "backgroung.png";
    public static final String BACKGROUND_PREFERENCE_NAME = "canvaTexture";

    private static File FOLDER;
    public static void setFolderName(File name) {
        FOLDER = name;
    }

    public static Bitmap loadBitmapFromFile(String fileName) {
        File bitmapFile = new File(FOLDER, fileName);
        return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
    }

    private static final String BACKGROUND_SCALE_PARAM_NAME = "backgroundScale";
    private static final float defaultBackgroundScale = 64;
    private static final float backgroundScaleMin = 32;
    public static float getBackgroundScaleMin() {return backgroundScaleMin;}
    private static final float backgroundScaleMax = 512;
    public static float getBackgroundScaleMax() {return backgroundScaleMax;}
    public static float getBackgroundScale() {
        return settings.getFloat(BACKGROUND_SCALE_PARAM_NAME,defaultBackgroundScale);
    }
    public static void setBackgroundScale(float scale) {
        settings.edit().putFloat(BACKGROUND_SCALE_PARAM_NAME, scale).apply();
    }

    private static final String BACKGROUND_OPACITY_PARAM_NAME = "backgroundOpacity";
    public static float getBackgroundOpacity() {
        return settings.getFloat(BACKGROUND_OPACITY_PARAM_NAME, 1.0f);
    }
    public static void setBackgroundOpacity(float opacity) {
        settings.edit().putFloat(BACKGROUND_OPACITY_PARAM_NAME, opacity).apply();
    }

    /**
     * Обновление фона приложения
     */
    public static void updateBackground() {

        //Получаем текущий фон и тип замощения
        //Формат строки
        //<Тип картинки>:<Доступ>:<Тип рисовки>
        //<Тип картинки> -- FILE или RESOURCE
        //<Доступ> -- имя файла (для FILE), номер ресурса (для RESOURCE)
        //<Тип рисовки> -- TILE или FILL
        BicycleDebugger.d(TAG, "Updating background");
        String background = settings.getString(BACKGROUND_PREFERENCE_NAME, "RESOURCE:" + R.drawable.grid + ":TILE");
        String[] params = background.split(":");
        paintingType = params[2].equals("FILL") ? Graphic.PaintingType.Fill : Graphic.PaintingType.Tile;
        if (params[0].equals("RESOURCE")) {
            int resourceId = Integer.parseInt(params[1]);
            World.setBackgroung(resourceId, paintingType, (int)getBackgroundScale());
        } else {
            Bitmap file = loadBitmapFromFile(params[1]);
            World.setBackgroung(file, paintingType, (int)getBackgroundScale());
        }

    }


    private static final String TAG = "LocalConfigs";


    private static boolean isListening = true;
    private static Graphic.PaintingType paintingType;

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
    public static final int canvaDraw=getNum();
    public static final int canvaSpeed=getNum();
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

    private static SharedPreferences settings;
    public static SharedPreferences getSettings() {
        return settings;
    }
    private static SharedPreferences.OnSharedPreferenceChangeListener settingsListener;

    public static void init(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
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
                    updateBackground();
                    World.reInit();
                }
            }
        };
        settings.registerOnSharedPreferenceChangeListener(settingsListener);
        fields=new ArrayList<>();
        fields.add(new BooleanField("bloodDraw"));
        fields.add(new FloatField("bloodVisibleTime"));
        fields.add(new IntegerField("bloodCount"));
        fields.add(new BooleanField("canvaDraw"));
        fields.add(new IntegerField("canvasSpeed"));
        fields.add(new FloatField("messageShowTime"));
        fields.add(new BooleanField("messageDraw"));
        fields.add(new FloatField("spawnsShowTime"));
        fields.add(new BooleanField("spawnsDraw"));
        fields.add(new IntegerField("timerTimer"));
        fields.add(new BooleanField("timerDraw"));
        fields.add(new IntegerField("worldGianSpawnProbability"));
        fields.add(new IntegerField("worldTowerSpawnProbability"));
        fields.add(new IntegerField("worldHorizontalBorders"));
        fields.add(new IntegerField("worldVerticalTopBorders"));
        fields.add(new IntegerField("worldVerticalBottomBorders"));
        settingsListener.onSharedPreferenceChanged(settings, null);
        BicycleDebugger.i("Configs", "Loaded successfully");
    }

    public static void applySettings() {
        settingsListener.onSharedPreferenceChanged(settings, null);
    }

    public static ArrayList<ConfigField> getFields() {
        ArrayList<ConfigField> configs=new ArrayList<>();
        for (ConfigField f: fields) configs.add(f.copy());
        return configs;
    }

    public static void setField(String name, String value) {
        for (ConfigField cf: fields) {
            if (cf.getName().toLowerCase().equals(name.toLowerCase())) {
                cf.setValue(value);
                break;
            }
        }
        World.reInit();
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

    public static Graphic.PaintingType getPaintingType() {
        return paintingType;
    }
}
