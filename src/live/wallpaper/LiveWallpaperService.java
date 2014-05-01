package live.wallpaper;

import android.content.SharedPreferences;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new RBEngine();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class RBEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private World world;

        public RBEngine() {
            SurfaceHolder holder = getSurfaceHolder();
            world = new World(holder, getApplicationContext());
        }

        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
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
            world.setSurfaceSize(width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
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


        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            world.doTouchEvent(event);
        }

    }
}