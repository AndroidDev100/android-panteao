package panteao.make.ready.utils.helpers;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.admanager.AdManagerAdView;

public class ADHelper {

    private static final ADHelper ourInstance = new ADHelper();
    private static AdManagerAdView adView;

    public static ADHelper getInstance(Context context) {
        adView = new AdManagerAdView(context);
        return ourInstance;
    }


    public AdManagerAdView getPublisherView() {
        return adView;
    }

    Activity pipAct;
    public void pipActivity(Activity episodeActivity) {
        this.pipAct=episodeActivity;
    }

    public Activity getPipAct() {
        return pipAct;
    }
}
