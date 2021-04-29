package panteao.make.ready.activities.usermanagment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.responseModels.LoginResponse.Data;
import panteao.make.ready.databinding.SignupActivityBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.tarcker.FCMEvents;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.tarcker.FCMEvents;

public class SignUpActivity extends BaseBindingActivity<SignupActivityBinding> implements AlertDialogFragment.AlertDialogListener {

    String regex = "(.)*(\\d)(.)*";
    private KsPreferenceKeys preference;
    private RegistrationLoginViewModel viewModel;
    private long mLastClickTime = 0;
    private String userID;
    private String userName;

    @Override
    public SignupActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return SignupActivityBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI(getBinding().root);
        callBinding();

    }

    private void callBinding() {
        viewModel = ViewModelProviders.of(SignUpActivity.this).get(RegistrationLoginViewModel.class);
        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        connectObservors();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //resetUI();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(SignUpActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().parentLayout.setVisibility(View.VISIBLE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            resetUI();
        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().parentLayout.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }

    String loginCallingFrom="";
    public void resetUI() {
        loginCallingFrom=getIntent().getStringExtra("loginFrom");
        getBinding().errorName.setVisibility(View.INVISIBLE);
        getBinding().errorEmail.setVisibility(View.INVISIBLE);
        getBinding().errorPassword.setVisibility(View.INVISIBLE);
        getBinding().etName.getText().clear();
        getBinding().etPassword.getText().clear();
        getBinding().etEmail.getText().clear();
    }

    public void connectObservors() {
        getBinding().errorName.setVisibility(View.INVISIBLE);
        getBinding().errorEmail.setVisibility(View.INVISIBLE);
        getBinding().errorPassword.setVisibility(View.INVISIBLE);

        getBinding().tvCancel.setOnClickListener(v -> finish());

        getBinding().llSignUp.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (CheckInternetConnection.isOnline(SignUpActivity.this)) {
                if (validateNameEmpty() && validateEmptyEmail() && validateEmail() && passwordCheck(getBinding().etPassword.getText().toString())) {
                    getBinding().errorName.setVisibility(View.INVISIBLE);
                    getBinding().errorEmail.setVisibility(View.INVISIBLE);
                    getBinding().errorPassword.setVisibility(View.INVISIBLE);
                    showLoading(getBinding().progressBar, true);
                    preference = KsPreferenceKeys.getInstance();
                    preference.setAppPrefAccessToken("");

                    viewModel.hitSignUpAPI(getBinding().etName.getText().toString(), getBinding().etEmail.getText().toString(), getBinding().etPassword.getText().toString()).observe(SignUpActivity.this, signupResponseAccessToken -> {
                        dismissLoading(getBinding().progressBar);

                        try {
                            if (signupResponseAccessToken.getResponseModel().getResponseCode() == 200) {
                                Gson gson = new Gson();
                                preference.setAppPrefAccessToken(signupResponseAccessToken.getAccessToken());
                                String stringJson = gson.toJson(signupResponseAccessToken.getResponseModel().getData());
                                saveUserDetails(stringJson, signupResponseAccessToken.getResponseModel().getData().getId(), true);
                                onBackPressed();
                                userID=signupResponseAccessToken.getResponseModel().getData().getId()+"";
                                userName=signupResponseAccessToken.getResponseModel().getData().getName()+"";
                                Log.d("manualsignup", userID);
                                Log.d("manualsignup",userName);

                                AppCommonMethod.trackFcmCustomEvent(getApplicationContext(),AppConstants.SIGN_UP_SUCCESS,"","","",0," ",0,"",0,0,"","",userID,userName);

                                //new ActivityLauncher(SignUpActivity.this).homeScreen(SignUpActivity.this, HomeActivity.class);
                                //finish();

                            } else if (signupResponseAccessToken.getResponseModel().getResponseCode() == 4901) {
                                showDialog(SignUpActivity.this.getResources().getString(R.string.error), signupResponseAccessToken.getDebugMessage().toString());
                            } else if (signupResponseAccessToken.getResponseModel().getResponseCode() == 400) {

                                showDialog(SignUpActivity.this.getResources().getString(R.string.error), signupResponseAccessToken.getDebugMessage().toString());
                            }
                        } catch (NullPointerException e) {

                        }


                    });
                }
            } else {
                dismissLoading(getBinding().progressBar);
                connectionValidation(false);
                //  new ToastHandler(SignUpActivity.this).show(SignUpActivity.this.getResources().getString(R.string.no_internet_connection));

            }
        });

        getBinding().tvAlreadyUser.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            finish();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class).putExtra("loginFrom",""));
        });

        getBinding().etName.setOnClickListener(view -> getBinding().errorName.setVisibility(View.INVISIBLE));


        getBinding().etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorName.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getBinding().etName.setOnTouchListener((view, motionEvent) -> {
            getBinding().errorName.setVisibility(View.INVISIBLE);
            return false;
        });


        getBinding().etEmail.setOnClickListener(view -> getBinding().errorEmail.setVisibility(View.INVISIBLE));
        getBinding().etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorEmail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getBinding().etEmail.setOnEditorActionListener((textView, i, keyEvent) -> {
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
            return false;
        });


        getBinding().etPassword.setLongClickable(false);
        getBinding().etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBinding().errorPassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getBinding().etPassword.setOnClickListener(view -> getBinding().errorPassword.setVisibility(View.INVISIBLE));

    }

    public void saveUserDetails(String response, int userID, boolean isManual) {
        Data fbLoginData = new Gson().fromJson(response, Data.class);
        Gson gson = new Gson();
        String stringJson = gson.toJson(fbLoginData);
        Log.d("responsefb",stringJson);

        if (isManual)
            preference.setAppPrefLoginType(AppConstants.UserLoginType.Manual.toString());
        else
            preference.setAppPrefLoginType(AppConstants.UserLoginType.FbLogin.toString());
        preference.setAppPrefProfile(stringJson);
        preference.setAppPrefLoginStatus(AppConstants.UserStatus.Login.toString());
        preference.setAppPrefUserId(String.valueOf(fbLoginData.getId()));
        preference.setAppPrefUserName(String.valueOf(fbLoginData.getName()));
        preference.setAppPrefUserEmail(String.valueOf(fbLoginData.getEmail()));
        AppCommonMethod.userId = String.valueOf(fbLoginData.getId());
        onBackPressed();
        //new ActivityLauncher(SignUpActivity.this).homeScreen(SignUpActivity.this, HomeActivity.class);

        try {
            trackEvent(String.valueOf(fbLoginData.getName()),String.valueOf(fbLoginData.getEmail()));
        }catch (Exception e){

        }
    }

    private void trackEvent(String name, String type) {
        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(EventConstant.Name, name);
        requestParam.addProperty(EventConstant.PlatformType, type);
        FCMEvents.getInstance().setContext(SignUpActivity.this).trackEvent(1,requestParam);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new ActivityLauncher(SignUpActivity.this).loginActivity(SignUpActivity.this, LoginActivity.class);

    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
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


    private boolean validateEmptyEmail() {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(getBinding().etEmail.getText().toString().trim())) {
            getBinding().errorEmail.setText(getResources().getString(R.string.email_check_signup));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        } else {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        }
        return check;
    }

    public boolean validateEmail() {
        boolean check = false;
        if (getBinding().etEmail.getText().toString().trim().matches(AppConstants.EMAIL_REGEX)) {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        } else {
            getBinding().errorEmail.setText(getResources().getString(R.string.valid_email));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        }
        return check;
    }


    public boolean passwordCheck(String password) {
       // String passwordRegex="^(?=.*[!&^%$#@()\\_+-])[A-Za-z0-9\\d!&^%$#@()\\_+-]{8,20}$";

        String passwordRegex="^[A-Za-z0-9\\d!&^%$#@()\\_+-]{6,20}$";

        boolean check = false;
        Pattern mPattern = Pattern.compile(passwordRegex);
        Matcher matcher = mPattern.matcher(password.toString());
        if(!matcher.find())
        {
            getBinding().errorPassword.setVisibility(View.VISIBLE);
            getBinding().errorPassword.setText(getResources().getString(R.string.strong_password_required));
          //  showDialog(SignUpActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.strong_password_required));
        }else {
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
            check = true;
        }
        return check;
    }

    public boolean stringContainsNumber(String s) {
        return Pattern.compile("[0-9]").matcher(s).find();
    }


    @Override
    public void onFinishDialog() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        getBinding().etPassword.setText("");
    }

}