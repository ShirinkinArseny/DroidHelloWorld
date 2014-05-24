package live.wallpaper.OpenGLIntegration;


import android.content.Context;
import android.opengl.GLSurfaceView;

public interface LifecycleRenderer extends GLSurfaceView.Renderer {
    public void onCreate(Context context);
    public void onPause();
    public void onDestroy();
    public void onResume();
}
