package live.wallpaper;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings extends PreferenceActivity {
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Preference preference = new Preference();
        getFragmentManager().beginTransaction().replace(android.R.id.content, preference).commit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private class Preference extends PreferenceFragment
    {
        //Хранение настроек
        SharedPreferences sharedPreferences;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}