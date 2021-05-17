package panteao.make.ready.activities.deeplink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;

import panteao.make.ready.R;
import panteao.make.ready.activities.article.ArticleActivity;
import panteao.make.ready.activities.detail.ui.EpisodeActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.utils.MediaTypeConstants;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.json.JSONException;

import io.branch.referral.Branch;

public class DeepLinkActivity extends AppCompatActivity {

    private long mLastClickTime = 0;
    private boolean viaIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
    }

    @Override
    protected void onStart() {
        super.onStart();
        branchRedirection();
    }

    private void branchRedirection() {
        Logger.e("onResume--", "Branch.getInstance()");
        Branch.getInstance().initSession((referringParams, error) -> {
            Logger.e("onResume-----", "Branch.getInstance()");
            KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
            int assestId = 0;
            String contentType = "";
            try {
                assestId = Integer.valueOf(referringParams.getString("id"));
                contentType = referringParams.getString("contentType");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Logger.e("ASSET TYPE", String.valueOf(viaIntent));

            if (!viaIntent) {
                preference.setAppPrefJumpTo(contentType);
                preference.setAppPrefBranchIo(true);
                preference.setAppPrefJumpBackId(assestId);

            }
            if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(DeepLinkActivity.this).seriesDetailScreen(DeepLinkActivity.this, SeriesDetailActivity.class, assestId);
            } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.VIDEO.toString())) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(DeepLinkActivity.this).articleScreen(DeepLinkActivity.this, ArticleActivity.class, assestId, "0", false);
            } else if (contentType.equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(DeepLinkActivity.this).episodeScreen(DeepLinkActivity.this, EpisodeActivity.class, assestId, "0", false);
            }
            finish();
        });

    }
}
