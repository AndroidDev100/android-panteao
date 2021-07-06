package panteao.make.ready.activities.redeemcoupon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import panteao.make.ready.R;
import panteao.make.ready.activities.redeemcoupon.viewModel.RedeemViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivityRedeemCouponBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;


public class RedeemCouponActivity extends BaseBindingActivity<ActivityRedeemCouponBinding> implements AlertDialogFragment.AlertDialogListener {
    private String token;
    private RedeemViewModel redeemViewModel;
    private boolean isloggedout = false;
    private boolean isCodeBlank = false;

    @Override
    public ActivityRedeemCouponBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityRedeemCouponBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callModel();
        token = new KsPreferenceKeys(this).getAppPrefAccessToken();

        toolBar();
        setClicks();

    }

    private void callModel() {
        redeemViewModel = ViewModelProviders.of(RedeemCouponActivity.this).get(RedeemViewModel.class);
    }

    private void setClicks() {
        getBinding().redeemCoupon.setOnClickListener(v -> {
            redeemCoupon();
        });
    }

    private void redeemCoupon() {
        String coupon = getBinding().couponEdt.getText().toString();
        if (!coupon.isEmpty()) {
            isCodeBlank=false;
            getBinding().progressBar.setVisibility(View.VISIBLE);
            redeemViewModel.redeemCoupon(token, coupon).observe(this, redeemCouponResponseModel -> {
                getBinding().progressBar.setVisibility(View.GONE);
                if (redeemCouponResponseModel != null) {
                    if (redeemCouponResponseModel.isStatus()) {
                        showDialog("", getResources().getString(R.string.redeemed_success));
                    } else {
                        if (redeemCouponResponseModel.getResponseCode() == 4302) {
                            logoutCall();
                           /* isloggedout = true;
                            showDialog(getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));*/
                        } else {
                            showDialog(getResources().getString(R.string.error), redeemCouponResponseModel.getDebugMessage());
                        }
                    }
                } else {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));

                }
            });
        }else {
            isCodeBlank=true;
            showDialog(getResources().getString(R.string.error), getResources().getString(R.string.enter_valid_coupon_code));
        }
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(new KsPreferenceKeys(this));
            hitApiLogout(this, new KsPreferenceKeys(this).getAppPrefAccessToken());
        } else {
            new ToastHandler(this).show(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    private void toolBar() {
        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(getResources().getString(R.string.redeem_coupon));

        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            logoutCall();
        }else if (isCodeBlank){

        }else {
            onBackPressed();
        }

    }
}
