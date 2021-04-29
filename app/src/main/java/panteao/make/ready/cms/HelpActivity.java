package panteao.make.ready.cms;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import panteao.make.ready.R;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivityHelpBinding;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ToolBarHandler;


public class HelpActivity extends BaseBindingActivity<ActivityHelpBinding> {
    private String type;


    @Override
    public ActivityHelpBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityHelpBinding.inflate(inflater);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("1")) {
            new ToolBarHandler(this).setHelpAction(getBinding(), HelpActivity.this.getResources().getString(R.string.term_condition));
        } else {
            new ToolBarHandler(this).setHelpAction(getBinding(), HelpActivity.this.getResources().getString(R.string.privacy_policy));
        }
        getBinding().toolbar.backLayout.setOnClickListener(v -> {
            onBackPressed();
        });


        getwebView();
    }

    private void getwebView() {
        getBinding().webView.getSettings().setJavaScriptEnabled(true);
        getBinding().webView.getSettings().setBuiltInZoomControls(true);
        getBinding().webView.getSettings().setUseWideViewPort(true);
        getBinding().webView.getSettings().setLoadWithOverviewMode(true);
        getBinding().webView.getSettings().setDomStorageEnabled(true);

        //  getBinding().webView.setBackgroundColor(getResources().getColor(R.color.theme_background_dark));

        String url;
        if (type.equalsIgnoreCase("1")) {
            url = SDKConfig.getInstance().getTermCondition_URL();
        } else if (type.equalsIgnoreCase("2")) {

            url = SDKConfig.getInstance().getPrivay_Policy_URL();
        } else {
            url = "https://www.google.co.in/";
        }
        Log.e("LOAD ERROR", url);

        getBinding().webView.loadUrl(url);
        getBinding().webView.getSettings().setBuiltInZoomControls(false);
        getBinding().webView.setWebViewClient(new InsideWebViewClient());

    }

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
                    new ToastHandler(HelpActivity.this).show(HelpActivity.this.getResources().getString(R.string.something_went_wrong_at_our_end));
                    onBackPressed();
                }
            });

        } catch (Exception e) {

        }
    }

}
