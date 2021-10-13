package panteao.make.ready.activities.profile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.userProfile.UserProfileResponse;
import panteao.make.ready.databinding.ProfileActivityNewBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;


public class ProfileActivityNew extends BaseBindingActivity<ProfileActivityNewBinding> implements AlertDialogFragment.AlertDialogListener {
    private RegistrationLoginViewModel viewModel;
    private KsPreferenceKeys preference;
    private boolean isloggedout = false;

    @Override
    public ProfileActivityNewBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ProfileActivityNewBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
    }

    private void connectionObserver() {
        preference = KsPreferenceKeys.getInstance();
        callModel();
        setToolbar();
        setOfflineData();
        if (NetworkConnectivity.isOnline(ProfileActivityNew.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }
    private void setToolbar(){
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setText(getResources().getString(R.string.my_profile));
    }

    private void setOfflineData() {
        getBinding().userNameWords.setText(AppCommonMethod.getUserName(preference.getAppPrefUserName()));
        getBinding().etName.setText(preference.getAppPrefUserName());
        getBinding().etEmail.setText(preference.getAppPrefUserEmail());
    }

    private void callModel() {
        viewModel = new ViewModelProvider(ProfileActivityNew.this).get(RegistrationLoginViewModel.class);
    }

    private void connectionValidation(boolean connected) {
        if (connected) {
            String token = preference.getAppPrefAccessToken();
            viewModel.hitUserProfile(ProfileActivityNew.this, token).observe(ProfileActivityNew.this, new Observer<UserProfileResponse>() {
                @Override
                public void onChanged(UserProfileResponse userProfileResponse) {
                    if (userProfileResponse != null) {
                        if (userProfileResponse.getStatus()) {
                            updateUI(userProfileResponse);
                        } else {
                            if (userProfileResponse.getResponseCode() == 4302) {
                                isloggedout = true;
                                logoutCall();
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                         onBackPressed();
                                        }
                                    });
                                }catch (Exception e){

                                }

                               // showDialog(getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                            }
                            if (userProfileResponse.getDebugMessage() != null) {
                                showDialog(ProfileActivityNew.this.getResources().getString(R.string.error), userProfileResponse.getDebugMessage().toString());
                            } else {
                                showDialog(ProfileActivityNew.this.getResources().getString(R.string.error), ProfileActivityNew.this.getResources().getString(R.string.something_went_wrong));
                            }
                        }
                    }
                }
            });
        } else {

        }

        getBinding().llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkConnectivity.isOnline(ProfileActivityNew.this)) {
                    if (validateNameEmpty()) {
                        showLoading(getBinding().progressBar, true);
                        String token = preference.getAppPrefAccessToken();
                        viewModel.hitUpdateProfile(ProfileActivityNew.this, token, getBinding().etName.getText().toString()).observe(ProfileActivityNew.this, new Observer<UserProfileResponse>() {
                            @Override
                            public void onChanged(UserProfileResponse userProfileResponse) {
                                dismissLoading(getBinding().progressBar);
                                if (userProfileResponse != null) {
                                    if (userProfileResponse.getStatus()) {
                                        showDialog("", ProfileActivityNew.this.getResources().getString(R.string.profile_update_successfully));
                                        updateUI(userProfileResponse);
                                    } else {
                                        if (userProfileResponse.getResponseCode() == 4302) {
                                            isloggedout = true;
                                            logoutCall();
                                            try {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        onBackPressed();
                                                    }
                                                });
                                            }catch (Exception e){

                                            }
                                           // showDialog(getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));

                                        } else {
                                            if (userProfileResponse.getDebugMessage() != null) {
                                                showDialog(ProfileActivityNew.this.getResources().getString(R.string.error), userProfileResponse.getDebugMessage().toString());
                                            } else {
                                                showDialog(ProfileActivityNew.this.getResources().getString(R.string.error), ProfileActivityNew.this.getResources().getString(R.string.something_went_wrong));
                                            }
                                        }
                                    }
                                }
                            }
                        });

                    }

                } else {
                    new ToastHandler(ProfileActivityNew.this).show(ProfileActivityNew.this.getResources().getString(R.string.no_internet_connection));
                }
            }
        });
    }

    public boolean validateNameEmpty() {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(getBinding().etName.getText().toString().trim())) {
            getBinding().errorName.setText(getResources().getString(R.string.empty_name));
            getBinding().errorName.setVisibility(View.VISIBLE);
        } else {
            check = true;
            getBinding().errorName.setVisibility(View.INVISIBLE);
        }
        return check;
    }


    private void updateUI(UserProfileResponse userProfileResponse) {
        try {
            getBinding().userNameWords.setText(AppCommonMethod.getUserName(userProfileResponse.getData().getName()));
            getBinding().etName.setText(userProfileResponse.getData().getName());
            getBinding().etName.setSelection(getBinding().etName.getText().length());
            getBinding().etEmail.setText(userProfileResponse.getData().getEmail());
            preference.setAppPrefUserName(String.valueOf(userProfileResponse.getData().getName()));
        } catch (Exception e) {

        }
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference);
            hitApiLogout(this, preference.getAppPrefAccessToken());
        } else {
           // new ToastHandler(this).show(getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            logoutCall();
        } else {
            onBackPressed();
        }

    }
}
