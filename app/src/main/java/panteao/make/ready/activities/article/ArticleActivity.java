package panteao.make.ready.activities.article;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import panteao.make.ready.R;
import panteao.make.ready.activities.usermanagment.ui.LoginActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ArticleActivityBinding;
import panteao.make.ready.networking.apistatus.APIStatus;
import panteao.make.ready.networking.responsehandler.ResponseModel;
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData;
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.player.ui.UserInteractionFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.ImageHelper;
import panteao.make.ready.utils.helpers.RailInjectionHelper;

import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ArticleActivity  extends BaseBindingActivity<ArticleActivityBinding> implements AlertDialogFragment.AlertDialogListener {

    private int assestId;
    private long brightCoveVideoId;
    private String tabId;
    public long videoPos = 0;
    private KsPreferenceKeys preference;
    private RailInjectionHelper railInjectionHelper;
    private EnveuVideoItemBean videoDetails;

    @Override
    public ArticleActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ArticleActivityBinding.inflate(inflater);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = KsPreferenceKeys.getInstance();
        if (getIntent().hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE);
                assestId = Objects.requireNonNull(extras).getInt(AppConstants.BUNDLE_ASSET_ID);
                videoPos = TimeUnit.SECONDS.toMillis(Long.parseLong(extras.getString(AppConstants.BUNDLE_DURATION)));
                brightCoveVideoId = Objects.requireNonNull(extras).getLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE);
                tabId = extras.getString(AppConstants.BUNDLE_DETAIL_TYPE, AppConstants.MOVIE_ENVEU);
            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
        findIds();
        callShimmer();
        callDetailApI(assestId);
    }

    private void findIds() {
        getBinding().backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolBar();

    }

    private void toolBar() {
        getBinding().toolbar.shareIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getBinding().toolbar.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShareDialogue();
            }
        });

    }

    public void callDetailApI(int id) {
        railInjectionHelper = ViewModelProviders.of(this).get(RailInjectionHelper.class);
/*
        railInjectionHelper.getAssetDetails(String.valueOf(assestId)).observe(ArticleActivity.this, enveuCommonResponse -> {
            if (enveuCommonResponse != null && enveuCommonResponse.getEnveuVideoItemBeans().size() > 0) {
                if (enveuCommonResponse.getEnveuVideoItemBeans().get(0).getResponseCode() == AppConstants.RESPONSE_CODE_SUCCESS) {
                    videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                    setValuesInUI(videoDetails);
                    callShareAnim();
                    stopShimmer();
                } else {
                    showDialog(ArticleActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                }
            }
        });
*/

        railInjectionHelper.getAssetDetailsV2(String.valueOf(assestId)).observe(ArticleActivity.this, assetResponse -> {
            if (assetResponse!=null){
                if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.START.name())){

                }
                else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.SUCCESS.name())){
                    parseAssetDetails(assetResponse);
                }
                else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.ERROR.name())){
                    if (assetResponse.getErrorModel() !=null && assetResponse.getErrorModel().getErrorCode()!=0){
                        showDialog(ArticleActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                    }

                }

                else if (assetResponse.getStatus().equalsIgnoreCase(APIStatus.FAILURE.name())){
                    showDialog(ArticleActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                }
            }

        });
    }

    private void parseAssetDetails(ResponseModel assetResponse) {
        RailCommonData enveuCommonResponse=(RailCommonData)assetResponse.getBaseCategory();

        if (enveuCommonResponse != null && enveuCommonResponse.getEnveuVideoItemBeans().size() > 0) {
            videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);

                videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                stopShimmer();
                setValuesInUI(videoDetails);
                callShareAnim();

        }
    }


    private void setValuesInUI(EnveuVideoItemBean videoDetails) {
        if (videoDetails!=null){
            int resId = R.anim.slide_down;
           // Animation animationUtils=AnimationUtils.loadAnimation(ArticleActivity.this,R.anim.slide_down);
            getBinding().articleDescription.setText(videoDetails.getLongDescription());
           // getBinding().txtLay.startAnimation(animationUtils);
            ImageHelper.getInstance(ArticleActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
            getBinding().articleTitle.setText(videoDetails.getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getBinding().articleDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                }
        }
    }

    private boolean jsutificationAllow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return true;
        }  else {
            return false;
        }
    }

    private void callShareAnim() {
        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.share_in);
        getBinding().toolbar.shareIcon.setVisibility(View.VISIBLE);
        getBinding().toolbar.shareIcon.startAnimation(animZoomIn);
    }

    private void stopShimmer() {
        Logger.e("stopShimmer", String.valueOf(brightCoveVideoId));

        if (brightCoveVideoId != 0) {
            getBinding().playerImage.setVisibility(View.GONE);
        } else {
            getBinding().playerImage.setVisibility(View.VISIBLE);
        }

        getBinding().seriesShimmer.setVisibility(View.GONE);
        getBinding().llParent.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
    }


    private void setUserInteractionFragment(int assestId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, assestId);
        args.putParcelable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails);
        UserInteractionFragment userInteractionFragment = new UserInteractionFragment();
        userInteractionFragment.setArguments(args);
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishDialog() {

    }

    private void callShimmer() {
        getBinding().seriesShimmer.setVisibility(View.VISIBLE);
        getBinding().mShimmer.seriesShimmerScroll1.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
        getBinding().mShimmer.seriesShimmerScroll2.setEnabled(false);
        getBinding().llParent.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().mShimmer.sfShimmer1.startShimmer();
        getBinding().mShimmer.sfShimmer2.startShimmer();
        getBinding().mShimmer.sfShimmer3.startShimmer();
        getBinding().mShimmer.flBackIconImage.bringToFront();
        getBinding().mShimmer.flBackIconImage.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void openLoginPage(String message) {
       // preference.setAppPrefJumpTo(AppConstants.ContentType.VIDEO.toString());
        preference.setAppPrefJumpBack(true);
        preference.setAppPrefIsEpisode(false);
        preference.setAppPrefJumpBackId(assestId);
        new ActivityLauncher(ArticleActivity.this).loginActivity(ArticleActivity.this, LoginActivity.class);

    }

    private long mLastClickTime = 0;
    private void openShareDialogue() {
        try {
            if (videoDetails!=null){
                String imgUrl = videoDetails.getThumbnailImage();
                int id = videoDetails.getId();
                String title = videoDetails.getTitle();
                String assetType = AppConstants.ContentType.ARTICLE.toString();

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                AppCommonMethod.openShareDialog(ArticleActivity.this, title, id, assetType, imgUrl, videoDetails.getSeries(), videoDetails.getSeason());

            }

        }catch (Exception e){

        }
    }

}
