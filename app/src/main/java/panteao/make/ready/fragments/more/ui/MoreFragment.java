package panteao.make.ready.fragments.more.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import panteao.make.ready.utils.helpers.downloads.DownloadHelper;
import panteao.make.ready.PanteaoApplication;
import panteao.make.ready.SDKConfig;
import panteao.make.ready.activities.homeactivity.viewmodel.HomeViewModel;
import panteao.make.ready.activities.myPurchases.ui.MyPurchasesActivity;
import panteao.make.ready.activities.notification.ui.NotificationActivity;
import panteao.make.ready.activities.profile.ui.ProfileActivityNew;
import panteao.make.ready.activities.redeemcoupon.RedeemCouponActivity;
import panteao.make.ready.activities.settings.ActivitySettings;
import panteao.make.ready.activities.usermanagment.ui.ChangePasswordActivity;
import panteao.make.ready.activities.watchList.ui.WatchListActivity;
import panteao.make.ready.baseModels.BaseBindingFragment;
import panteao.make.ready.cms.HelpActivity;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.fragments.more.adapter.MoreListAdapter;
import panteao.make.ready.R;
import panteao.make.ready.activities.membershipplans.ui.MemberShipPlanActivity;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.beanModel.responseModels.RegisterSignUpModels.DataResponseRegister;
import panteao.make.ready.callbacks.commonCallbacks.MoreItemClickListener;
import panteao.make.ready.databinding.FragmentMoreBinding;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.constants.SharedPrefesConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.helpers.CheckInternetConnection;

import panteao.make.ready.utils.helpers.NetworkConnectivity;
import panteao.make.ready.utils.helpers.SharedPrefHelper;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.downloads.KTDownloadHelper;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




@SuppressWarnings("StatementWithEmptyBody")
public class MoreFragment extends BaseBindingFragment<FragmentMoreBinding> implements MoreItemClickListener, AlertDialogFragment.AlertDialogListener {
    public IntentFilter intentFilter;
    boolean isloggedout = false;
    boolean isHomeDirect = false;
    private android.content.res.Resources res;
    private KsPreferenceKeys preference;
    private boolean isLogin;
    private List<String> mListVerify;
    private List<String> mListLogin;
    private AppSyncBroadcast appSyncBroadcast;
    private HomeViewModel viewModel;
    private boolean flagLogIn;
    private boolean flagVerify;
    private long mLastClickTime = 0;
    private MoreFragmentInteraction mListener;

    @Override
    public FragmentMoreBinding inflateBindingLayout() {
        return FragmentMoreBinding.inflate(inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        res = getResources();
        //     FacebookSdk.sdkInitialize(getActivity());
//
//        ((HomeActivity) Objects.requireNonNull(getActivity())).updateApi(click -> {
//            if (click) {
//                if (!NetworkConnectivity.isOnline(Objects.requireNonNull(getActivity()))) {
//                    getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
//                    getBinding().profileFrame.setVisibility(View.GONE);
//                }
//            }
//        });

    }

    public void startMoreFragment() {
        modelCall();
//
//        if (NetworkConnectivity.isOnline(Objects.requireNonNull(getActivity()))) {
//            getBinding().noConnectionLayout.setVisibility(View.GONE);
//            getBinding().profileFrame.setVisibility(View.VISIBLE);
//            modelCall();
//        } else {
//            getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
//            getBinding().profileFrame.setVisibility(View.GONE);
//        }

        getBinding().connection.retryTxt.setOnClickListener(view -> startMoreFragment());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (preference != null) {
            isLogin = preference.getAppPrefLoginStatus();
            if (isLogin) {
                getBinding().userNameWords.setText(AppCommonMethod.getUserName(preference.getAppPrefUserName()));
                getBinding().usernameTv.setText(preference.getAppPrefUserName());
            }
        }

        Logger.i("TAG", "Tried to unregister the reciver when it's not registered");

    }

    @Override
    public void onResume() {
        super.onResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction("PROFILE_UPDATE");
        appSyncBroadcast = new AppSyncBroadcast();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver(appSyncBroadcast, intentFilter);
        isloggedout = false;
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        startMoreFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).unregisterReceiver(appSyncBroadcast);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                Logger.i("TAG", "Tried to unregister the reciver when it's not registered");
            } else {
                throw e;
            }
        }
    }

    String[] label2;
    String[] label3;
    KTDownloadHelper downloadHelper;

    private void modelCall() {

        viewModel = new ViewModelProvider(Objects.requireNonNull(requireActivity())).get(HomeViewModel.class);
        String[] label1 = getActivity().getResources().getStringArray(R.array.more_with_verify);

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.resetLanguage("th", getActivity());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.resetLanguage("en", getActivity());
        }

        //using for logged-in and logged-out user
       /* if (SDKConfig.getInstance().isDownloadEnable()){
            label2 = getActivity().getResources().getStringArray(R.array.more_with_login_download);
            label3= getActivity().getResources().getStringArray(R.array.more_logout_download);
        }else {
            label2 = getActivity().getResources().getStringArray(R.array.more_with_login);
            label3= getActivity().getResources().getStringArray(R.array.more_logout);
        }*/

        label2 = getActivity().getResources().getStringArray(R.array.more_with_login);
        label3 = getActivity().getResources().getStringArray(R.array.more_logout);

        mListVerify = new ArrayList<>();
        mListVerify.addAll(Arrays.asList(label1));
        List<String> mListLogOut = new ArrayList<>(Arrays.asList(label3));
        mListLogin = new ArrayList<>();
        mListLogin.addAll(Arrays.asList(label2));

        preference = KsPreferenceKeys.getInstance();
        isLogin = preference.getAppPrefLoginStatus();
        if (isLogin) {
            String tempResponse = preference.getAppPrefUser();
            if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
                setVerify();
            } else {
                String tempResponseApi = preference.getAppPrefProfile();
                setVerifyApi();
            }
        } else {
            getBinding().loginBtn.setVisibility(View.VISIBLE);

            AppCommonMethod.guestTitle(getBaseActivity(), getBinding().userNameWords, getBinding().usernameTv, preference);
            getBinding().usernameTv.setVisibility(View.VISIBLE);
            setUIComponets();
        }

        if (getActivity() != null) {
            downloadHelper = new KTDownloadHelper(getActivity());
        }


    }

    private void setUIComponets() {

        MoreListAdapter adapter = new MoreListAdapter(getActivity(), mList, MoreFragment.this, isLogin, SDKConfig.getInstance().isDownloadEnable());
        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getBinding().recyclerViewMore.setAdapter(adapter);

//        getBinding().loginBtn.setOnClickListener(v -> );
        getBinding().loginBtn.setOnClickListener(v -> {
            mListener.onLoginClicked();
        });
    }

    public void clickEvent() {
        try {
            preference = KsPreferenceKeys.getInstance();
            String token = preference.getAppPrefAccessToken();
            viewModel.hitUserProfile(getContext(), token).observe(getActivity(), userProfileResponse -> {
                if (userProfileResponse != null) {
                    if (userProfileResponse.getStatus()) {
                        updateUI();
                    } else {
                        if (userProfileResponse.getDebugMessage() != null) {

                        } else {

                        }
                    }
                }
            });

        } catch (Exception ignored) {

        }

    }

    private void updateUI() {
        try {
            preference.setAppPrefUserName(String.valueOf(userProfileResponse.getData().getName()));
            preference.setAppPrefUserEmail(String.valueOf(userProfileResponse.getData().getEmail()));
            getBinding().userNameWords.setText(AppCommonMethod.getUserName(preference.getAppPrefUserName()));
            getBinding().usernameTv.setText(preference.getAppPrefUserName());
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick() {
        boolean loginStatus = preference.getAppPrefLoginStatus();
        String isFacebook = preference.getAppPrefLoginType();
        if (caption.equals(getString(R.string.profile))) {
            if (loginStatus)

                new ActivityLauncher(getActivity()).ProfileActivityNew(getActivity(), ProfileActivityNew.class);

            else
                mListener.onLoginClicked();
        } else if (caption.equals(getString(R.string.change_password))) {
            if (loginStatus)
              /*  if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {

                } else {*/ {
                new ActivityLauncher(getActivity()).changePassword(getActivity(), ChangePasswordActivity.class);
                AppCommonMethod.trackFcmEvent("Change password", "", getContext());
            }

            /* }*/
            else
                mListener.onLoginClicked();
        } else if (caption.equals(getString(R.string.my_purchases))) {
            if (loginStatus) {
                new ActivityLauncher(getActivity()).myPurchases(getActivity(), MyPurchasesActivity.class,AppConstants.FROM_MORE_FRAGMENT);
                AppCommonMethod.trackFcmEvent(AppConstants.MY_PURCHASES, "", getContext());
            } else
                mListener.onLoginClicked();
        } else if (caption.equals(getString(R.string.notification))) {
            if (loginStatus)
                new ActivityLauncher(getActivity()).notificationActivity(getActivity(), NotificationActivity.class);
            else
                mListener.onLoginClicked();
        } else if (caption.equals(getString(R.string.term_condition))) {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("Terms and Conditons", "", getContext());

            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("Terms and Conditons", "", getContext());

            }
            //    Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), SampleActivity.class).putExtra("type", "1").putExtra("url",getString(R.string.term_condition)));
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), HelpActivity.class).putExtra("type", "1"));
        } else if (caption.equals(getString(R.string.privacy_policy))) {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("Privacy Policy", "", getContext());

            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("Privacy Policy", "", getContext());

            }
            // Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), WebViewFlutterActivity.class).putExtra("type", "2").putExtra("url",getString(R.string.privacy_policy)));
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), HelpActivity.class).putExtra("type", "2"));

            //About us

        } else if (caption.equals(getString(R.string.about_us))) {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("About Us", "", getContext());

            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());

                AppCommonMethod.trackFcmEvent("About Us", "", getContext());

            }
            // Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), WebViewFlutterActivity.class).putExtra("type", "2").putExtra("url",getString(R.string.privacy_policy)));
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), HelpActivity.class).putExtra("type", "3"));


        } else if (caption.equals(getString(R.string.my_watchlist))) {

            if (loginStatus) {
                new ActivityLauncher(getActivity()).watchHistory(getActivity(), WatchListActivity.class, getString(R.string.my_watchlist), false);

                AppCommonMethod.trackFcmEvent("My Watchlist", "", getContext());

            } else
                mListener.onLoginClicked();

        } else if (caption.equals(getString(R.string.my_history))) {
            if (loginStatus) {
                new ActivityLauncher(getActivity()).watchHistory(getActivity(), WatchListActivity.class, Objects.requireNonNull(getActivity()).getResources().getString(R.string.my_history), true);
                AppCommonMethod.trackFcmEvent("My Watch History", "", getContext());

            } else
                mListener.onLoginClicked();
        } else if (caption.equals(getString(R.string.my_downloads))) {
            if (loginStatus)
                new ActivityLauncher(getActivity()).launchMyDownloads();
            else
                mListener.onLoginClicked();
        } else if (caption.equals(Objects.requireNonNull(getActivity()).getResources().getString(R.string.sign_out))) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {

                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            flagLogIn = true;
            flagVerify = false;
            showAlertDialog();
//                AppCommonMethod.trackFcmCustomEvent(getContext(), AppConstants.LOGOUT, "Main - More", "", "", "", 0, " ", 0, "", 0, 0, "", "");


        } else if (caption.equalsIgnoreCase(getActivity().getResources().getString(R.string.membership_plan))) {
            if (NetworkConnectivity.isOnline(getActivity())) {
                Intent intent = new Intent(getActivity(), MemberShipPlanActivity.class);
                startActivity(intent);
                AppCommonMethod.trackFcmEvent("Membership and Plans", "", getContext());

            } else {
                new ToastHandler(getActivity()).show(getResources().getString(R.string.no_connection));
            }
            /*if (loginStatus) {


            } else {
                mListener.onLoginClicked();
            }*/

        } else if (caption.equalsIgnoreCase(getActivity().getResources().getString(R.string.redeem_coupon))) {
            if (loginStatus) {
                Intent intent = new Intent(getActivity(), RedeemCouponActivity.class);
                startActivity(intent);
                AppCommonMethod.trackFcmEvent("Redeem Coupon", "", getContext());

            } else {
                mListener.onLoginClicked();
            }

        } else if (caption.equalsIgnoreCase(getActivity().getResources().getString(R.string.setting_title))) {
            if (loginStatus) {

                Intent intent = new Intent(getActivity(), ActivitySettings.class);
                startActivity(intent);
                AppCommonMethod.trackFcmEvent("Settings", "", getContext());

            } else
                mListener.onLoginClicked();

        }

    }


    public void hitApiVerifyUser() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().progressBar.bringToFront();
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(getActivity()))) {
            String token = preference.getAppPrefAccessToken();
            getBinding().progressBar.setVisibility(View.VISIBLE);
            viewModel.hitVerify(token).observe(getActivity(), jsonObject ->
                    {
                        getBinding().progressBar.setVisibility(View.GONE);
                        if (jsonObject.isStatus()) {

                        } else {
                            if (jsonObject.getResponseCode() == 401) {
                                isloggedout = true;
                                flagVerify = false;
                                showDialog();
                            }
                        }
                    }
            );
        } else
            new ToastHandler(getActivity()).show(getActivity().getResources().getString(R.string.no_internet_connection));
    }

    private void showAlertDialog() {
        FragmentManager fm = getFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(title, msg, getResources().getString(R.string.ok), getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    private void showDialog() {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    public void hitApiLogout() {
        if (getActivity() != null)
            if (CheckInternetConnection.isOnline(Objects.requireNonNull(getActivity()))) {
                String isFacebook = preference.getAppPrefLoginType();
                if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
                    LoginManager.getInstance().logOut();
                }

                String token = preference.getAppPrefAccessToken();
                showLoading(getBinding().progressBar, getActivity());
                dismissLoading(getBinding().progressBar, getActivity());
                String strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
                String strCurrentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
                int languagePosition = KsPreferenceKeys.getInstance().getAppPrefLanguagePos();
                int downloadOverWifi = KsPreferenceKeys.getInstance().getDownloadOverWifi();
                String downloadEmail = KsPreferenceKeys.getInstance().getLoginEmailForDownloadCheck();
                // preference.setAppPrefLoginStatus(false);
                preference.clear();
//                preference.setAppPrefUserId("");
                Log.d("getIdLogout", preference.getAppPrefUserId());
                KsPreferenceKeys.getInstance().setLoginEmailForDownloadCheck(downloadEmail);
                KsPreferenceKeys.getInstance().setDownloadOverWifi(downloadOverWifi);
                KsPreferenceKeys.getInstance().setCurrentTheme(strCurrentTheme);
                KsPreferenceKeys.getInstance().setAppLanguage(strCurrentLanguage);
                KsPreferenceKeys.getInstance().setAppPrefLanguagePos(languagePosition);
                new SharedPrefHelper(getActivity()).setInt(SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 3);
                modelCall();
                Logger.w("currentLang-->>", strCurrentLanguage);
                if (strCurrentLanguage.equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                    AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
                } else if (strCurrentLanguage.equalsIgnoreCase("English")) {
                    AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
                }

                //AppCommonMethod.updateLanguage(strCurrentLanguage, getActivity());
                viewModel.hitLogout(false, token).observe(getActivity(), jsonObject -> {
                    int msg = Objects.requireNonNull(jsonObject).get(AppConstants.API_RESPONSE_CODE).getAsInt();
                    if (msg == AppConstants.RESPONSE_CODE_ERROR) {
                        flagLogIn = false;
                        dismissLoading(getBinding().progressBar, getActivity());
                    } else {

                    }
                });

                try {
//                    if (downloadHelper!=null && getActivity()!=null){
//                        downloadHelper.deleteAllVideos(getActivity());
//                    }

                    if (downloadHelper != null) {
                        if (downloadHelper.getManager() != null) {
                            downloadHelper.getManager().pauseDownloads();
                        }
                    }

                } catch (Exception ignored) {

                }

            } else {
                dismissLoading(getBinding().progressBar, getActivity());

                new ToastHandler(getActivity()).show(getActivity().getResources().getString(R.string.no_internet_connection));
            }
    }

    public void hitApiConfig() {
        ApiInterface endpoint = RequestConfig.getClient().create(ApiInterface.class);
        Call<ResponseConfig> call = endpoint.getConfiguration("true");
        call.enqueue(new Callback<ResponseConfig>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConfig> call, @NonNull Response<ResponseConfig> response) {
                dismissLoading(getBinding().progressBar, getActivity());
                if (response.body() != null) {
                    AppCommonMethod.urlPoints = response.body().getData().getCloudFrontEndpoint();
                    ResponseConfig cl = response.body();
                    KsPreferenceKeys ksPreferenceKeys = KsPreferenceKeys.getInstance();
                    Gson gson = new Gson();
                    String json = gson.toJson(cl);
                    AppCommonMethod.urlPoints = /*AppConstants.PROFILE_URL +*/ response.body().getData().getImageTransformationEndpoint();

                    ksPreferenceKeys.setAppPrefLastConfigHit(String.valueOf(System.currentTimeMillis()));
                    ksPreferenceKeys.setAppPrefLoginStatus(false);
                    ksPreferenceKeys.setAppPrefAccessToken("");
                    ksPreferenceKeys.setAppPrefConfigResponse(json);
                    ksPreferenceKeys.setAppPrefVideoUrl(response.body().getData().getCloudFrontVideoEndpoint());
                    ksPreferenceKeys.setAppPrefAvailableVersion(response.body().getData().getUpdateInfo().getAvailableVersion());
                    ksPreferenceKeys.setAppPrefCfep(AppCommonMethod.urlPoints);
                    ksPreferenceKeys.setAppPrefConfigVersion(String.valueOf(response.body().getData().getConfigVersion()));
                    ksPreferenceKeys.setAppPrefServerBaseUrl(response.body().getData().getServerBaseURL());


                    // versionValidator.version(false, currentVersion, currentVersion);
                    Glide.with(getActivity()).load(getActivity().getResources().getDrawable(R.drawable.default_profile_pic)).into(getBinding().ivProfilePic);
                    modelCall();
                    if (isHomeDirect) {

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConfig> call, @NonNull Throwable t) {
                dismissLoading(getBinding().progressBar, getActivity());
            }
        });

    }

    public void updateAppSync() {
        isLogin = preference.getAppPrefLoginStatus();
        if (isLogin) {
            setVerify();
        }

    }

    @Override
    public void onFinishDialog() {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance());
        }
        if (flagLogIn) {
            AppCommonMethod.guestTitle(getBaseActivity(), getBinding().userNameWords, getBinding().usernameTv, preference);
            hitApiLogout();
            AppCommonMethod.trackFcmCustomEvent(getContext(), AppConstants.LOGOUT, "", "", "", 0, " ", 0, "", 0, 0, "", "", "", "");

            flagLogIn = false;
        } else if (flagVerify) {
            hitApiVerifyUser();
            flagVerify = false;
        } else if (isloggedout) {
            hitApiLogout();
            AppCommonMethod.trackFcmCustomEvent(getContext(), AppConstants.LOGOUT, "", "", "", 0, " ", 0, "", 0, 0, "", "", "", "");
//            preference.setAppPrefUserId("");
//            Log.d("getIdLogout",preference.getAppPrefUserId());

            isHomeDirect = true;
            isloggedout = false;

        }
    }

    public void setVerify() {
        String tempResponse = preference.getAppPrefUser();
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            AppUserModel dataModel = new Gson().fromJson(tempResponse, AppUserModel.class);
            getBinding().loginBtn.setVisibility(View.GONE);
            getBinding().usernameTv.setVisibility(View.VISIBLE);

            getBinding().userNameWords.setText(AppCommonMethod.getUserName(dataModel.getName()));
            getBinding().usernameTv.setText(dataModel.getName());
            if (!StringUtils.isNullOrEmpty(dataModel.getProfilePicURL()))
                try {
                    setProfilePic();
                } catch (Exception e) {
                    Logger.e("MoreFragment", "" + e.toString());
                }

            if (dataModel != null) {
                /*if (dataModel.isVerified()) {
                    setUIComponets(mListLogin, true);
                } else
                    setUIComponets(mListVerify, true);*/
                setUIComponets();
            } else {
                setUIComponets();
            }

        }
    }

    public void setVerifyApi() {
        DataResponseRegister ddModel;
        ddModel = new Gson().fromJson(tempResponse, DataResponseRegister.class);

        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            getBinding().loginBtn.setVisibility(View.GONE);
            getBinding().usernameTv.setVisibility(View.VISIBLE);
            getBinding().userNameWords.setText(AppCommonMethod.getUserName(preference.getAppPrefUserName()));
            getBinding().usernameTv.setText(preference.getAppPrefUserName());
            setUIComponets();
           /* if (ddModel.isVerified()) {
                setUIComponets(mListLogin, true);
            } else
                setUIComponets(mListVerify, true);*/
        }

    }

    public void setProfilePic() {
        String url1 = preference.getAppPrefCfep();
        StringBuilder stringBuilder = new StringBuilder();
        if (key.contains("http")) {
            stringBuilder.append(url1).append("/fit-in/200x200/filters:quality(100):max_bytes(400)/").append(key);
        } else {
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String url2 = AppConstants.PROFILE_FOLDER;
            stringBuilder = stringBuilder.append(url1).append(url2).append(key);
        }
        Glide.with(Objects.requireNonNull(getActivity()))
                .asBitmap()
                .load(stringBuilder.toString())
                .apply(AppCommonMethod.options)
                .into(getBinding().ivProfilePic);

    }

    public class AppSyncBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                updateAppSync();
            } catch (Exception e) {
                Logger.e("MoreFragment", "" + e.toString());

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MoreFragmentInteraction) getActivity();
    }

    public interface MoreFragmentInteraction {
        void onLoginClicked();
    }

}
