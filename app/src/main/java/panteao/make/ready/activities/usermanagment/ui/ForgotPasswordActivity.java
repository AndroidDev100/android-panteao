package panteao.make.ready.activities.usermanagment.ui;


import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.databinding.ForgotPasswordBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToolBarHandler;
import panteao.make.ready.baseModels.BaseBindingActivity;

public class ForgotPasswordActivity extends BaseBindingActivity<ForgotPasswordBinding> implements AlertDialogFragment.AlertDialogListener {

    private RegistrationLoginViewModel viewModel;
    private long mLastClickTime = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBinding();
    }

    @Override
    public ForgotPasswordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ForgotPasswordBinding.inflate(inflater);
    }

    private void callBinding() {
        viewModel = ViewModelProviders.of(ForgotPasswordActivity.this).get(RegistrationLoginViewModel.class);
        connectionObserver();
        connectObservors();
    }


    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(ForgotPasswordActivity.this)) {
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
        getBinding().connection.retryTxt.setOnClickListener(view -> {
            getBinding().etPasswordRecoveryEmail.setText("");
            connectionObserver();
        });
    }


    public void connectObservors() {
        new ToolBarHandler(ForgotPasswordActivity.this).setAction(getBinding().toolbar, "ForgotPasswordActivity");

        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());

        getBinding().tvSendEmail.setOnClickListener(view -> {
            if (CheckInternetConnection.isOnline(ForgotPasswordActivity.this)) {
                if (validateEmptyEmail() && validateEmail()) {

                    showProgressDialog();
                    //hit api to update
                    viewModel.hitForgotPasswordApi(getBinding().etPasswordRecoveryEmail.getText().toString().trim()).observe(ForgotPasswordActivity.this, jsonObject -> {
                        dismissLoading(getBinding().progressBar);
                        if (jsonObject.getCode() == 200){
                            showDialog("", getResources().getString(R.string.forgot_password_response));
                        }

                        else{
                            if (jsonObject.getDebugMessage()!=null && !jsonObject.getDebugMessage().equalsIgnoreCase("")){
                                showDialog(ForgotPasswordActivity.this.getResources().getString(R.string.error),jsonObject.getDebugMessage());
                            }else {
                                showDialog("", getResources().getString(R.string.forgot_password_response));
                            }

                        }

                    });
                }
            } else
                connectionObserver();
            //new ToastHandler(ForgotPasswordActivity.this).show(ForgotPasswordActivity.this.getResources().getString(R.string.no_internet_connection));
        });

    }


    public void showProgressDialog() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 800) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        showLoading(getBinding().progressBar, true);

    }


    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }


    private boolean validateEmptyEmail() {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(getBinding().etPasswordRecoveryEmail.getText().toString().trim())) {
            getBinding().errorEmail.setText(getResources().getString(R.string.empty_string));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        } else {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        }
        return check;
    }

    public boolean validateEmail() {
        boolean check = false;
        if (getBinding().etPasswordRecoveryEmail.getText().toString().trim().matches(AppConstants.EMAIL_REGEX)) {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        } else {
            getBinding().errorEmail.setText(getResources().getString(R.string.valid_email));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        }
        return check;
    }

    @Override
    public void onFinishDialog() {
        onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}