package panteao.make.ready.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import panteao.make.ready.BuildConfig;
import panteao.make.ready.activities.membershipplans.ui.MemberShipPlanActivity;
import panteao.make.ready.activities.settings.downloadsettings.DownloadSettings;
import panteao.make.ready.activities.videoquality.ui.VideoQualityActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.databinding.SettingsActivityBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;

import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.utils.inAppBilling.BillingProcessor;
import panteao.make.ready.utils.inAppBilling.InAppProcessListener;
import panteao.make.ready.utils.inAppBilling.RestoreSubcriptionsDialod;
import panteao.make.ready.utils.inAppBilling.RestoreSubscriptionCallback;


public class ActivitySettings extends BaseBindingActivity<SettingsActivityBinding> implements View.OnClickListener, InAppProcessListener, RestoreSubcriptionsDialod.RestoreSubcriptionsDialodListener {

    @Override
    public SettingsActivityBinding inflateBindingLayout() {
        return SettingsActivityBinding.inflate(inflater);
    }

    private BillingProcessor bp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBinding().switchThemeNew.setChecked(KsPreferenceKeys.getInstance().getDownloadOverWifi() == 1);

        bp = new BillingProcessor(ActivitySettings.this,this);
        bp.initializeBillingProcessor();
        getBinding().buildNumber.setText(getResources().getString(R.string.app_name) + "  v" + BuildConfig.VERSION_NAME);

        getBinding().switchThemeNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    new SharedPrefHelper(ActivitySettings.this).setInt(SharedPrefesConstants.DOWNLOAD_OVER_WIFI, 1);
                }else {
                    new SharedPrefHelper(ActivitySettings.this).setInt(SharedPrefesConstants.DOWNLOAD_OVER_WIFI, 0);
                }
            }
        });

        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setTheme(R.style.MyMaterialTheme_Base_Light);

            getBinding().switchTheme.setChecked(false);
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark);
            getBinding().switchTheme.setChecked(true);
        }


        toolBar();
        checkLanguage();

        getBinding().downloadSettings.setOnClickListener(this);
//        getBinding().parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ActivitySettings.this, ChangeLanguageActivity.class);
//                startActivity(intent);
//                AppCommonMethod.trackFcmEvent("Language Selection","",ActivitySettings.this,0);
//
//            }
//        });

        getBinding().videoQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySettings.this, VideoQualityActivity.class);
                startActivity(intent);
                AppCommonMethod.trackFcmEvent("Streaming Setting","",ActivitySettings.this);

            }
        });

        getBinding().restoreSuscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBinding().progressBar.setVisibility(View.VISIBLE);
                if (bp!=null){
                    if (bp.isReady()){
                        bp.queryPurchases(new RestoreSubscriptionCallback() {
                            @Override
                            public void subscriptionStatus(boolean status, String message) {
                                getBinding().progressBar.setVisibility(View.GONE);
                                if (status){
                                    showAlertDialog();
                                }else {
                                    if (message.contains("We could not restore your subscription at this time. Please try again after some time. If issue persists, please contact support")){
                                        showAlertDialog();
                                    }else {
                                        showAlertDialog();
                                    }

                                }

                            }
                        });
                    }else {
                        getBinding().progressBar.setVisibility(View.GONE);
                    }
                }else {
                    getBinding().progressBar.setVisibility(View.GONE);
                }
            }
        });


        getBinding().switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (getBinding().switchTheme.isChecked()) {


                    //  getBinding().switchTheme.setChecked(false);

                    KsPreferenceKeys.getInstance().setCurrentTheme(AppConstants.DARK_THEME);
                } else {
                    //    getBinding().switchTheme.setChecked(true);
                    KsPreferenceKeys.getInstance().setCurrentTheme(AppConstants.LIGHT_THEME);
                }

                recreate();
            }
        });
        getBinding().switchTheme.setOnClickListener(v -> {


        });

        try {
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus()){
               // getBinding().downloadLayout.setVisibility(View.VISIBLE);
            }else {
//                getBinding().downloadLayout.setVisibility(View.GONE);
               // getBinding().downloadLayout.setVisibility(View.VISIBLE);

            }
        }catch (Exception ignored){

        }
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RestoreSubcriptionsDialod alertDialog = RestoreSubcriptionsDialod.newInstance(title, msg, getResources().getString(R.string.ok), getResources().getString(R.string.cancel),type);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    private void toolBar() {
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(getResources().getString(R.string.setting_title));

        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void checkLanguage() {

//        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
//        String currentLanguage = preference.getAppLanguage();
//        if (currentLanguage.isEmpty()) {
//            getBinding().languageText.setText("English");
//        } else {
//            if (currentLanguage.equalsIgnoreCase("English")) {
//                getBinding().languageText.setText(currentLanguage);
//
//            } else {
//                getBinding().languageText.setText(getString(R.string.language_thai));
//            }
//        }
//        String qualityName = preference.getQualityName();
//        setQualityText(qualityName);

    }

    private void setQualityText() {
        if (qualityName.isEmpty()) {
            getBinding().qualityText.setText(getString(R.string.auto));

        } else {
            if (qualityName.equalsIgnoreCase("Auto")){
                getBinding().qualityText.setText(getString(R.string.auto));
            }else if (qualityName.equalsIgnoreCase("Low")){
                getBinding().qualityText.setText(getString(R.string.low));
            }
            else if (qualityName.equalsIgnoreCase("Medium")){
                getBinding().qualityText.setText(getString(R.string.medium));
            }
            else if (qualityName.equalsIgnoreCase("High")){
                getBinding().qualityText.setText(getString(R.string.high));
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getBinding() != null) {
            setQualityText();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_settings: {
                Intent intent = new Intent(ActivitySettings.this, DownloadSettings.class);
                startActivity(intent);
            }
            break;
////            case R.id.parent_layout: {
////                Intent intent = new Intent(ActivitySettings.this, ChangeLanguageActivity.class);
////                startActivity(intent);
////            }
//            break;
        }
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onPurchasesUpdated() {

    }

    @Override
    public void onListOfSKUFetched() {

    }

    @Override
    public void onBillingError() {

    }

    @Override
    public void onFinishDialog() {
        if (btnType==1){
            if (NetworkConnectivity.isOnline(ActivitySettings.this)) {
                Intent intent = new Intent(ActivitySettings.this, MemberShipPlanActivity.class);
                startActivity(intent);

            }else {
                new ToastHandler(ActivitySettings.this).show(getResources().getString(R.string.no_connection));
            }
        }else if (btnType==2){

        }else {

        }
    }
}
