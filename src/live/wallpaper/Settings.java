package live.wallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Settings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Preference preference = new Preference();
        getFragmentManager().beginTransaction().replace(android.R.id.content, preference).commit();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //Configs.init(this);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Configs.init(this);
    }

    private class Preference extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}