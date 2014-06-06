package live.wallpaper.Ads;

import android.content.Context;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by peter on 23.05.14.
 */
public class AdBuilder {

    private static final String bannerAtSettings = "ca-app-pub-9892437184583781/8615970953";
    public static final int bannerAtSettingsID = 0;
    private static String getIDByIntID(int id) {
        switch (id) {
            case bannerAtSettingsID:
                return bannerAtSettings;
            default:
                return null;
        }
    }

    private static AdRequest.Builder addTestDevices(AdRequest.Builder adBuilder) {
        //Здесь можно указать настройки рекламы
        //Например, отключить её для тестовых девайсов
        return adBuilder;
    }

    public static View createAdView(Context context, int bannerID) {
        // Create the adView
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getIDByIntID(bannerID));

        // Initiate a generic request to load it with an ad
        AdRequest adRequest = addTestDevices(new AdRequest.Builder()).build();
        adView.loadAd(adRequest);

        return adView;
    }
}
