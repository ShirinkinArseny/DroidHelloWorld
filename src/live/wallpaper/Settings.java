package live.wallpaper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Settings extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Settings", "Activity started!");
        setContentView(R.layout.settings);
    }
}