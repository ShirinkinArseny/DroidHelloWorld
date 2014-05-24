package live.wallpaper;

import android.content.Context;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new RBEngine(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Загружаем стандартные значения настроек, если пользователь ещё не зашел в настройки
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }

    public class RBEngine extends Engine {

        private final World world;

        public RBEngine(Context context) {
            world = new World(context);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            world.stopPainting();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            world.setSurface(holder, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
            world.setSurface(holder, metrics.widthPixels, metrics.heightPixels);
            world.run();
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                world.resumePainting();
            } else {
                world.pausePainting();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            world.stopPainting();
        }

    }
}