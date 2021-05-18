package panteao.make.ready.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import panteao.make.ready.activities.settings.downloadsettings.DownloadSettings;
import panteao.make.ready.activities.videoquality.ui.ChangeLanguageActivity;
import panteao.make.ready.activities.videoquality.ui.VideoQualityActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.databinding.SettingsActivityBinding;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;

import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import panteao.make.ready.activities.settings.downloadsettings.DownloadSettings;
import panteao.make.ready.activities.videoquality.ui.ChangeLanguageActivity;
import panteao.make.ready.activities.videoquality.ui.VideoQualityActivity;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


public class ActivitySettings extends BaseBindingActivity<SettingsActivityBinding> implements View.OnClickListener {

    @Override
    public SettingsActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return SettingsActivityBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                AppCommonMethod.trackFcmEvent("Streaming Setting","",ActivitySettings.this,0);

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
                getBinding().downloadLayout.setVisibility(View.VISIBLE);
            }else {
                getBinding().downloadLayout.setVisibility(View.GONE);
            }
        }catch (Exception ignored){

        }
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

    private void setQualityText(String qualityName) {
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
            setQualityText(KsPreferenceKeys.getInstance().getQualityName());
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
}
