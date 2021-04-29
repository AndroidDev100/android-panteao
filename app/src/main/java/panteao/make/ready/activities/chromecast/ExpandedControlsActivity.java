package panteao.make.ready.activities.chromecast;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brightcove.cast.DefaultExpandedControllerActivity;
import com.bumptech.glide.Glide;
import panteao.make.ready.R;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public class ExpandedControlsActivity extends DefaultExpandedControllerActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.cast_expanded_controller_activity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EnveuVideoItemBean asset = (EnveuVideoItemBean) getIntent().getSerializableExtra("Asset");
        if (asset != null)
            ((TextView) findViewById(R.id.asset_title)).setText(asset.getTitle());
        findViewById(R.id.blurred_background_image_view).setVisibility(View.GONE);
        Glide.with(this)
                .asBitmap()
                .load(asset.getPosterURL())
                .apply(AppCommonMethod.castOptions)
                .into((ImageView) findViewById(R.id.background_place_holder_image_view));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}
