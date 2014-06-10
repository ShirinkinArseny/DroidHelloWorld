package com.acidspacecompany.epicwallpaperfight;

import android.content.Context;
import android.preference.PreferenceManager;
import com.acidspacecompany.epicwallpaperfight.Ads.AdBuilder;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Graphic;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.LifecycleRenderer;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.OpenGLES20LiveWallpaperService;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class LiveWallpaperService extends OpenGLES20LiveWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new OpenGLES20LiveWallpaperService.GLEngine();
    }

    public LifecycleRenderer getRenderer() {
        //Загружаем стандартные значения настроек, если пользователь ещё не зашел в настройки
        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.preferences, false);
        //Создаем рекламу
        AdBuilder.initAd(getBaseContext());
        return new MyRenderer();
    }

    class MyRenderer implements LifecycleRenderer {

        private World world;

        @Override
        public void onCreate(Context context) {
            world = new World(context);
        }

        @Override
        public void onPause() {
            world.pausePainting();
        }

        @Override
        public void onDestroy() {
            world.stopPainting();
            Graphic.destroy();
        }

        @Override
        public void onResume() {
            world.resumePainting();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            Graphic.init(getBaseContext());
            World.init();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            world.setSurface(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //Рисуем
            world.updateAndDraw();
        }
    }
}
