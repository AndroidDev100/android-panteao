package panteao.make.ready.activities.usermanagment.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.databinding.NewPasswordScreenBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.CheckInternetConnection;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panteao.make.ready.baseModels.BaseBindingActivity;

import static panteao.make.ready.R.font.roboto_regular;

public class ChangePasswordActivity extends BaseBindingActivity<NewPasswordScreenBinding> implements AlertDialogFragment.AlertDialogListener {


    Typeface font;
    private KsPreferenceKeys preference;
    private String token;
    private RegistrationLoginViewModel viewModel;
    private long mLastClickTime = 0;
    private boolean isloggedout = false;

    @Override
    public NewPasswordScreenBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return NewPasswordScreenBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
        callBinding();
        isloggedout = false;

    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(ChangePasswordActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().root.setVisibility(View.VISIBLE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().root.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }

    private void callBinding() {
        viewModel = ViewModelProviders.of(ChangePasswordActivity.this).get(RegistrationLoginViewModel.class);

        getBinding().tvChangePassword.setClickable(true);
        getBinding().radioPasswordEye.setChecked(false);
        getBinding().confirmPasswordEye.setChecked(false);
        preference = KsPreferenceKeys.getInstance();
        token = preference.getAppPrefAccessToken();
        String tempResponse = preference.getAppPrefProfile();

        Logger.e("", "APP_PREF_PROFILE" + tempResponse);
        // getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setVisibility(View.VISIBLE);
        getBinding().etNewPassword.setLongClickable(false);
        getBinding().etConfirmNewPassword.setLongClickable(false);
        getBinding().toolbar.screenText.setText(ChangePasswordActivity.this.getResources().getString(R.string.change_password));

        getBinding().etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        getBinding().etConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        font = ResourcesCompat.getFont(ChangePasswordActivity.this, roboto_regular);
        getBinding().etNewPassword.setTypeface(font);
        getBinding().etConfirmNewPassword.setTypeface(font);

        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());

        getBinding().etNewPassword.setOnTouchListener((view, motionEvent) -> {
            getBinding().errorNewPwd.setVisibility(View.INVISIBLE);
            return false;
        });


        getBinding().radioPasswordEye.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                getBinding().etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                getBinding().etNewPassword.setTypeface(font);
            } else {
                getBinding().etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                getBinding().etNewPassword.setSelection(getBinding().etNewPassword.getText().length());
                getBinding().etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                hideSoftKeyboard(getBinding().radioPasswordEye);
                getBinding().etNewPassword.setTypeface(font);
            }
        });

        getBinding().confirmPasswordEye.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                getBinding().etConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                getBinding().etConfirmNewPassword.setTypeface(font);
            } else {
                getBinding().etConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                getBinding().etConfirmNewPassword.setSelection(getBinding().etNewPassword.getText().length());
                getBinding().etConfirmNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                hideSoftKeyboard(getBinding().confirmPasswordEye);
                getBinding().etConfirmNewPassword.setTypeface(font);
            }
        });

        getBinding().etConfirmNewPassword.setOnTouchListener((view, motionEvent) -> {
            getBinding().errorNewPwdConfirm.setVisibility(View.INVISIBLE);
            return false;
        });

        getBinding().tvChangePassword.setOnClickListener(view -> {
            if (CheckInternetConnection.isOnline(ChangePasswordActivity.this)) {
                if (
                        editviewEmpty(getBinding().etNewPassword, getBinding().errorNewPwd, ChangePasswordActivity.this.getResources().getString(R.string.please_enter_new_password)) &&
                                editViewValidity(getBinding().etNewPassword, getBinding().errorNewPwd, ChangePasswordActivity.this.getResources().getString(R.string.strong_password_required)) &&
                                editviewEmpty(getBinding().etConfirmNewPassword, getBinding().errorNewPwdConfirm, ChangePasswordActivity.this.getResources().getString(R.string.please_confirm_password)) &&
                                compareBothPwd(getBinding().etNewPassword.getText().toString().trim(), getBinding().etConfirmNewPassword.getText().toString().trim())
                ) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    {
                        getBinding().tvChangePassword.setClickable(false);
                        getBinding().progressBar.setVisibility(View.VISIBLE);
                        viewModel.hitApiChangePwd(getBinding().etNewPassword.getText().toString(), token,ChangePasswordActivity.this).observe(ChangePasswordActivity.this, jsonObject -> {
                            Logger.e("", "response" + jsonObject);
                            getBinding().tvChangePassword.setClickable(true);
                            getBinding().progressBar.setVisibility(View.GONE);
                            if (jsonObject.getResponseCode() == 200 || jsonObject.getResponseCode()==2000) {
                                showDialog("", getResources().getString(R.string.password_changed_successfully));
                            } else if (jsonObject.getResponseCode() == 401) {
                                isloggedout = true;
                                logoutCall();
                                //showDialog(ChangePasswordActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                            }
                            else if (jsonObject.getResponseCode() == 403) {
                                isloggedout = true;
                                logoutCall();
                               // showDialog(ChangePasswordActivity.this.getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));
                            }
                        });

                    }
                }
            } else
                new ToastHandler(ChangePasswordActivity.this).show(ChangePasswordActivity.this.getResources().getString(R.string.no_internet_connection));

        });
    }


    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }


    private boolean editviewEmpty(EditText editText, TextView errorView, String msg) {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(editText.getText().toString().trim())) {
            errorView.setText(msg);
            errorView.setVisibility(View.VISIBLE);
        } else {
            check = true;
            errorView.setVisibility(View.INVISIBLE);
        }
        return check;
    }

    private boolean editViewValidity(EditText editText, TextView errorView, String string) {

        //String passwordRegex="^(?=.*[!&^%$#@()\\_+-])[A-Za-z0-9\\d!&^%$#@()\\_+-]{8,20}$";
        String passwordRegex="^[A-Za-z0-9\\d!&^%$#@()\\_+-]{6,20}$";
        boolean check = false;
        Pattern mPattern = Pattern.compile(passwordRegex);
        Matcher matcher = mPattern.matcher(editText.getText().toString());
        if(!matcher.find())
        {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(getResources().getString(R.string.strong_password_required));
        }else {
            errorView.setVisibility(View.INVISIBLE);
            check = true;
        }

        return check;

    }

    private boolean stringContainsNumber(String s) {
        return Pattern.compile("[0-9]").matcher(s).find();
    }


    private boolean compareBothPwd(String pwd1, String pwd2) {
        boolean check = false;
        if (pwd1.equals(pwd2)) {
            check = true;
            getBinding().errorNewPwdConfirm.setVisibility(View.INVISIBLE);
        } else {
            getBinding().errorNewPwdConfirm.setVisibility(View.VISIBLE);
            getBinding().errorNewPwdConfirm.setText(ChangePasswordActivity.this.getResources().getString(R.string.confirm_pwd_not_match));
        }
        return check;


    }


    @Override
    public void onFinishDialog() {
        if (isloggedout)
            logoutCall();
            //hitApiLogout(ChangePasswordActivity.this, preference.getAppPrefAccessToken());
        else
            onBackPressed();
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(ChangePasswordActivity.this))) {
            clearCredientials(preference);
            hitApiLogout(ChangePasswordActivity.this, preference.getAppPrefAccessToken());
        } else {
           // new ToastHandler(ChangePasswordActivity.this).show(ChangePasswordActivity.this.getResources().getString(R.string.no_internet_connection));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getBinding().etNewPassword.setText("");
        getBinding().etConfirmNewPassword.setText("");
    }


}
