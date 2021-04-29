package panteao.make.ready.activities.privacypolicy.ui;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import panteao.make.ready.R;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivityTermsConditionBinding;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.baseModels.BaseBindingActivity;

public class WebViewActivity extends BaseBindingActivity<ActivityTermsConditionBinding> {

    @Override
    public ActivityTermsConditionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityTermsConditionBinding.inflate(inflater);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String webViewHeading = getIntent().getStringExtra(AppConstants.WEB_VIEW_HEADING);
        String webViewUrl = getIntent().getStringExtra(AppConstants.WEB_VIEW_URL);
        getwebView(webViewHeading, webViewUrl);
    }

    private void getwebView(String webViewHeading, String webViewUrl) {
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(webViewHeading);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);

        getBinding().webView.getSettings().setJavaScriptEnabled(true);
        getBinding().webView.getSettings().setBuiltInZoomControls(true);
        getBinding().webView.getSettings().setUseWideViewPort(true);
        getBinding().webView.getSettings().setLoadWithOverviewMode(true);
        getBinding().webView.getSettings().setDomStorageEnabled(true);


       /* getBinding().webView.getSettings().setJavaScriptEnabled(true);
        getBinding().webView.getSettings().setBuiltInZoomControls(true);
        getBinding().webView.getSettings().setUseWideViewPort(true);
        getBinding().webView.getSettings().setLoadWithOverviewMode(true);
        getBinding().webView.loadUrl(webViewUrl);
        getBinding().webView.setWebViewClient(new WebViewActivity.InsideWebViewClient());*/

        getBinding().webView.loadUrl(webViewUrl);
        getBinding().webView.getSettings().setBuiltInZoomControls(false);
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


   /* private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }*/

    private class InsideWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                view.reload();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.e("LOAD ERROR", new Gson().toJson(error));

            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:

                    break;
                case SslError.SSL_EXPIRED:

                    break;
                case SslError.SSL_IDMISMATCH:

                    break;
                case SslError.SSL_NOTYETVALID:

                    break;
            }
            handler.cancel();
            showErrorToast();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request.getUrl().toString().startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(request.getUrl().toString()));
                startActivity(intent);
                view.reload();
                return true;
            } else {
                return false;
            }
        }


    }

    private void showErrorToast() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new ToastHandler(WebViewActivity.this).show(WebViewActivity.this.getResources().getString(R.string.something_went_wrong_at_our_end));
                    onBackPressed();
                }
            });

        } catch (Exception e) {

        }
    }



}

