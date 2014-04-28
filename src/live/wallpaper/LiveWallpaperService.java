package live.wallpaper;

import android.content.SharedPreferences;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new SampleEngine();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class SampleEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private World painting;

        SampleEngine() {
            SurfaceHolder holder = getSurfaceHolder();
            painting = new World(holder, getApplicationContext()
            );
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
            painting.stopPainting();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            painting.setSurfaceSize(width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            painting.run();

        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                painting.resumePainting();
            } else {
                // remove listeners and callbacks here
                painting.pausePainting();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            painting.stopPainting();
        }


        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            painting.doTouchEvent(event);
        }

    }
}