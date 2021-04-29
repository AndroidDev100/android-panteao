package panteao.make.ready.activities.privacypolicy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.databinding.ActivityContactUsBinding;

import static panteao.make.ready.BuildConstants.CONTACT_US;


public class ContactUsActivity extends BaseBindingActivity<ActivityContactUsBinding> {

    @Override
    public ActivityContactUsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityContactUsBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getwebView();
    }

    private void getwebView() {
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(this.getResources().getString(R.string.Contact_us));
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().webView.getSettings().setJavaScriptEnabled(true);
        getBinding().webView.getSettings().setBuiltInZoomControls(true);
        getBinding().webView.getSettings().setUseWideViewPort(true);
        getBinding().webView.getSettings().setLoadWithOverviewMode(true);
        getBinding().webView.loadUrl(CONTACT_US);
        getBinding().webView.setWebViewClient(new InsideWebViewClient());

        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
