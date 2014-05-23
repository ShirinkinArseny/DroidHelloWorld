package live.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import live.wallpaper.Ads.AdBuilder;

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundleSavedInstance) {
            LinearLayout linearLayout = (LinearLayout) super.onCreateView(inflater, container, bundleSavedInstance);
            linearLayout.addView(AdBuilder.createAdView(getBaseContext(), AdBuilder.bannerAtSettingsID));
            return linearLayout;
        }
    }
}