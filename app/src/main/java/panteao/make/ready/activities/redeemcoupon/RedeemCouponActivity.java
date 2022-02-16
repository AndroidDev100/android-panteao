package panteao.make.ready.activities.redeemcoupon;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import panteao.make.ready.R;
import panteao.make.ready.activities.redeemcoupon.viewModel.RedeemViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.databinding.ActivityRedeemCouponBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


public class RedeemCouponActivity extends BaseBindingActivity<ActivityRedeemCouponBinding> implements AlertDialogFragment.AlertDialogListener {
    private String token;
    private RedeemViewModel redeemViewModel;
    private final boolean isloggedout = false;
    private boolean isCodeBlank = false;

    @Override
    public ActivityRedeemCouponBinding inflateBindingLayout() {
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
        redeemViewModel = new ViewModelProvider(RedeemCouponActivity.this).get(RedeemViewModel.class);
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
                        showDialog();
                    } else {
                        if (redeemCouponResponseModel.getResponseCode() == 4302) {
                            logoutCall();
                           /* isloggedout = true;
                            showDialog(getResources().getString(R.string.logged_out), getResources().getString(R.string.you_are_logged_out));*/
                        } else {
                            showDialog();
                        }
                    }
                } else {
                    showDialog();

                }
            });
        }else {
            isCodeBlank=true;
            showDialog();
        }
    }

    private void logoutCall() {
        if (CheckInternetConnection.isOnline(this)) {
            clearCredientials(new KsPreferenceKeys(this));
            hitApiLogout(new KsPreferenceKeys(this).getAppPrefAccessToken());
        } else {
            new ToastHandler(this).show(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void showDialog() {
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
