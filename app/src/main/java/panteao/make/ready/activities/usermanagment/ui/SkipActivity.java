package panteao.make.ready.activities.usermanagment.ui;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;

import panteao.make.ready.activities.instructor.ui.InstructorActivity;
import panteao.make.ready.activities.show.ui.EpisodeActivity;
import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.activities.homeactivity.ui.HomeActivity;
import panteao.make.ready.activities.series.ui.SeriesDetailActivity;
import panteao.make.ready.beanModel.configBean.ResponseConfig;
import panteao.make.ready.beanModel.requestParamModel.RequestParamRegisterUser;
import panteao.make.ready.beanModel.responseModels.RegisterSignUpModels.DataResponseRegister;
import panteao.make.ready.beanModel.responseModels.SignUp.DataModel;
import panteao.make.ready.databinding.SkipBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


public class SkipActivity extends BaseBindingActivity<SkipBinding> implements AlertDialogFragment.AlertDialogListener {

    long getTimeStampDOB = 0;
    boolean isForceSkip = false;
    boolean isloggedout = false;
    boolean uploadImageClick = false;
    boolean logoutBackpress = false;
    private RegistrationLoginViewModel viewModel;
    private DataModel model;
    private RequestParamRegisterUser requestParamRegisterUser;
    private KsPreferenceKeys preference;
    private Uri picUri;
    //  DataResponseRegister skipModel;
//    private AmazonS3 s3;
//    private TransferUtility transferUtility;
    private int counter = 0;
    private DataResponseRegister dataResponseRegister;
    private long mLastClickTime = 0;
    private boolean skipClick = false;

    public static String getFileNameFromUrl(String url) {

        Logger.e("", "ProfilePic Name" + url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0]);
        Logger.e("", "ProfilePic path" + url);


        return url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    @Override
    public SkipBinding inflateBindingLayout() {
        return SkipBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBinding();

    }

    private void callBinding() {
        viewModel = new ViewModelProvider(SkipActivity.this).get(RegistrationLoginViewModel.class);

//        credentialsProvider();
//        setTransferUtility();
        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            getBundleValue();
        }
        skipClick = false;
        clickListners();

    }

    public void getBundleValue() {
        preference = KsPreferenceKeys.getInstance();
        uploadImageClick = false;
        if (getIntent().hasExtra(AppConstants.EXTRA_REGISTER_USER)) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle(AppConstants.EXTRA_REGISTER_USER);
                model = extras.getParcelable(AppConstants.STRING_USER_DETAIL);
            }
            dataResponseRegister = new DataResponseRegister();
            try {
                dataResponseRegister.setDateOfBirth(model.getDateOfBirth());
                dataResponseRegister.setEmail(model.getEmail());
                dataResponseRegister.setExpiryDate(model.getExpiryDate());
                dataResponseRegister.setGender(model.getGender());
                dataResponseRegister.setId(model.getId());
                dataResponseRegister.setName(model.getName());
                dataResponseRegister.setPassword(model.getPassword());
                dataResponseRegister.setPhoneNumber(model.getPhoneNumber());
                dataResponseRegister.setProfilePicURL(model.getProfilePicURL());
                dataResponseRegister.setStatus(model.getStatus());
                dataResponseRegister.setVerified(Boolean.parseBoolean(model.getVerified()));
                dataResponseRegister.setVerificationDate(model.getVerificationDate());
            } catch (Exception e) {
                Logger.e("SkipActivity", "" + e.toString());
            }

        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
    }

    public void clickListners() {

        getBinding().llSaveProfile.setOnClickListener(view -> {
            if (CheckInternetConnection.isOnline(SkipActivity.this)) {
                if (setDataRequestModel()) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    getBinding().llSaveProfile.setEnabled(false);
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                    getBinding().progressBar.bringToFront();
                    if (picUri != null) {
                        //setFileToUpload();
                    } else hitApiUpdateProfile();
                }
            } else
                new ToastHandler(SkipActivity.this).show(SkipActivity.this.getResources().getString(R.string.no_internet_connection));
        });


        getBinding().tvSkipRegister.setOnClickListener(view -> {
            if (CheckInternetConnection.isOnline(SkipActivity.this)) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                skipClick = true;
                AppCommonMethod.afterLogin = true;
                onPause();
                //  skipCall();
                //  new ActivityLauncher(SkipActivity.this).homeScreen(SkipActivity.this, HomeActivity.class);
            } else
                new ToastHandler(SkipActivity.this).show(SkipActivity.this.getResources().getString(R.string.no_internet_connection));
        });

        getBinding().tvDateOfBirth.setOnClickListener(view -> getTimeStampDOB = AppCommonMethod.showDatePicker(getBinding().tvDateOfBirth, SkipActivity.this));

        getBinding().profileLayout.setOnClickListener(view -> {
            uploadImageClick = true;
            uploadImage();
        });
    }

    public void hitApiUpdateProfile() {
        viewModel.hitRegisterSignUpAPI(SkipActivity.this, requestParamRegisterUser).observe(SkipActivity.this, signUpResponseModel -> {
            getBinding().llSaveProfile.setEnabled(true);
            getBinding().progressBar.setVisibility(View.GONE);
            if (Objects.requireNonNull(signUpResponseModel).isStatus()) {
                dataResponseRegister = signUpResponseModel.getData();
                AppCommonMethod.afterLogin = true;
                showDialog();
            } else {
                showLoading(getBinding().progressBar);
                AppCommonMethod.afterLogin = true;
                isloggedout = true;
                showDialog();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (getBinding().progressBar != null)
                            dismissLoading(getBinding().progressBar);
                    }
                }, 10000);
                /*if (signUpResponseModel.getResponseCode() == 401) {
                    isForceSkip = true;
                    skipClick = true;
                    AppCommonMethod.afterLogin = true;
                    showDialog(SkipActivity.this.getResources().getString(R.string.register), getResources().getString(R.string.profile_registered_successfully));

                }*/
            }
        });

    }

//    private void credentialsProvider() {
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                COGNITO_IDENTITY, Regions.AP_SOUTHEAST_1
//
//        );
//        setAmazonS3Client(credentialsProvider);
//    }

//    private void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) {
//        s3 = new AmazonS3Client(credentialsProvider);
//        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
//    }
//
//    @SuppressWarnings("deprecation")
//    public void setTransferUtility() {
//        transferUtility = new TransferUtility(s3, getApplicationContext());
//    }

    public void setProfilePic() {
        String url1 = preference.getAppPrefCfep();
        if (StringUtils.isNullOrEmpty(url1)) {
            url1 = AppCommonMethod.urlPoints;
            preference.setAppPrefCfep(url1);
        }
        String url2 = AppConstants.PROFILE_FOLDER;

        Glide.with(SkipActivity.this)
                .asBitmap()
                .load(url1 + url2 + key)
                .apply(AppCommonMethod.options)
                .into(getBinding().ivProfilePic);

    }

//    public void setFileToUpload() {
//        TransferObserver transferObserver = transferUtility.upload(
//                IMAGE_BUCKET_ADDRESS, getFileNameFromUrl(picUri.getPath()),
//                new File(picUri.getPath())
//        );
//        transferObserverListener(transferObserver);
//    }

//    private void transferObserverListener(TransferObserver transferObserver) {
//        transferObserver.setTransferListener(new TransferListener() {
//            @Override
//            public void onStateChanged(int id, TransferState state) {
//                if (state == TransferState.COMPLETED) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                    setProfilePic(transferObserver.getKey());
//                    requestParamRegisterUser.setProfilePicURL(transferObserver.getKey());
//                    hitApiUpdateProfile();
//
//                }
//
//            }
//
//            @Override
//            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                int progress = (int) ((double) bytesCurrent * 100 / bytesTotal);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//
//            }
//
//            @Override
//            public void onError(int id, Exception ex) {
//                Logger.e("error", "error");
//            }
//
//        });
//    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    public boolean setDataRequestModel() {
        requestParamRegisterUser = new RequestParamRegisterUser();
        requestParamRegisterUser.setId(model.getId());
        requestParamRegisterUser.setEmail(model.getEmail());
        requestParamRegisterUser.setPassword(model.getPassword());
        requestParamRegisterUser.setName(model.getName());
        requestParamRegisterUser.setVerified(true);
        requestParamRegisterUser.setStatus(model.getStatus());
        requestParamRegisterUser.setExpiryDate(model.getExpiryDate());
        requestParamRegisterUser.setProfileStep(AppConstants.PROFILE_SETUP);
        if (model.getVerificationDate() == null)
            requestParamRegisterUser.setVerificationDate(null);
        else
            requestParamRegisterUser.setVerificationDate(Long.parseLong(model.getVerificationDate()));
        if (picUri != null)
            requestParamRegisterUser.setProfilePicURL(new File(picUri.getPath()).toString());

        if (AppCommonMethod.getTimeStampDOB > 0)
            requestParamRegisterUser.setDateOfBirth(AppCommonMethod.getTimeStampDOB);
        if (getBinding().rbGenderMale.isChecked())
            requestParamRegisterUser.setGender(AppConstants.GENDER_MALE);
        if (getBinding().rbGenderFemale.isChecked())
            requestParamRegisterUser.setGender(AppConstants.GENDER_FEMALE);

        if (getBinding().etMobileNumber.getText().toString().length() > 0)
            requestParamRegisterUser.setPhoneNumber(getBinding().etMobileNumber.getText().toString());

        String token = preference.getAppPrefAccessToken();
        if (!StringUtils.isNullOrEmptyOrZero(token))
            requestParamRegisterUser.setAccessToken(token);
        else
            Toast.makeText(SkipActivity.this, "Token Not Available", Toast.LENGTH_LONG).show();
        return true;

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!uploadImageClick) {
            onFinishDialog();
        }

    }

    public void setLoginUserData() {
        preference.setAppPrefProfile(json);
        preference.setAppPrefLoginStatus(true);
        preference.setAppPrefLoginType(AppConstants.UserLoginType.Manual.toString());
        preference.setAppPrefUserId(id);
    }

    public void uploadImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String picturePath = "";
            switch (requestCode) {

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        picUri = resultUri;
                        Glide.with(this).load(resultUri).into(getBinding().ivProfilePic);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                    uploadImageClick = false;
                    break;
            }

        }
    }

    @Override
    public void onFinishDialog() {
        PrintLogging.printLog("onFinishDialog");
        if (isloggedout) {
            String token = preference.getAppPrefAccessToken();
            hitApiLogout(token);
        } else if (dataResponseRegister != null) {
            showLoading(getBinding().progressBar);
            counter = 0;
            Gson gson = new Gson();
            String stringJson = gson.toJson(dataResponseRegister);
            preference.setAppPrefLoginType(AppConstants.UserLoginType.Manual.toString());
            preference.setAppPrefUserId(dataResponseRegister.getId());
            preference.setAppPrefProfile(stringJson);
            preference.setAppPrefLoginStatus(true);
            setLoginUserData();

            if (isForceSkip) {
                isForceSkip = false;
                if (preference.getAppPrefJumpBack()) {
                    int assetId = preference.getAppPrefJumpBackId();
                    if (preference.getAppPrefJumpTo().equalsIgnoreCase(getResources().getString(R.string.series))) {
                        new ActivityLauncher(SkipActivity.this).seriesDetailScreen(SkipActivity.this, SeriesDetailActivity.class, assetId);
                    } else {
                        if (preference.getAppPrefIsEpisode()){
                            new ActivityLauncher(SkipActivity.this).episodeScreen(SkipActivity.this, EpisodeActivity.class, assetId, "0", false);
                        } else {
                            new ActivityLauncher(SkipActivity.this).detailScreen(SkipActivity.this, InstructorActivity.class, assetId, "0", false);
                        }
                    }
                } else
                    new ActivityLauncher(SkipActivity.this).homeScreen(SkipActivity.this, HomeActivity.class);


            }
        }
    }

    public void hitApiLogout(String token) {
        preference = KsPreferenceKeys.getInstance();
        String isFacebook = preference.getAppPrefLoginType();
        if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
            LoginManager.getInstance().logOut();
        }
        // hitApiConfig(context);
        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

        Call<JsonObject> call = endpoint.getLogout(false);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {
                if (response.code() == 404) {
                    Logger.e("BaseActivity", "404 Error");
                } else if (response.code() == 200 || response.code() == 401) {
                    preference.clear();
                    logoutBackpress = true;
                    hitApiConfig(context);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                //pDialog.call();
            }
        });


    }


    public void hitApiConfig(Context context) {
        ApiInterface endpoint = RequestConfig.getClient().create(ApiInterface.class);

        Call<ResponseConfig> call = endpoint.getConfiguration("true");
        call.enqueue(new Callback<ResponseConfig>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConfig> call, @NonNull retrofit2.Response<ResponseConfig> response) {
                if (response.body() != null) {
                    dismissLoading(getBinding().progressBar);
                    AppCommonMethod.urlPoints = response.body().getData().getImageTransformationEndpoint();
                    ResponseConfig cl = response.body();
                    KsPreferenceKeys sharedPrefHelper = KsPreferenceKeys.getInstance();
                    Gson gson = new Gson();
                    String json = gson.toJson(cl);
                    sharedPrefHelper.setAppPrefLoginStatus(false);
                    sharedPrefHelper.setAppPrefAccessToken("");

                    sharedPrefHelper.setAppPrefConfigResponse(json);
                    sharedPrefHelper.setAppPrefLastConfigHit(String.valueOf(System.currentTimeMillis()));
                    AppCommonMethod.urlPoints = /*AppConstants.PROFILE_URL +*/ response.body().getData().getImageTransformationEndpoint();
                    sharedPrefHelper.setAppPrefVideoUrl(response.body().getData().getCloudFrontVideoEndpoint());
                    sharedPrefHelper.setAppPrefAvailableVersion(response.body().getData().getUpdateInfo().getAvailableVersion());
                    sharedPrefHelper.setAppPrefCfep(AppCommonMethod.urlPoints);
                    sharedPrefHelper.setAppPrefConfigVersion(String.valueOf(response.body().getData().getConfigVersion()));
                    sharedPrefHelper.setAppPrefServerBaseUrl(response.body().getData().getServerBaseURL());

                    preference.setAppPrefLoginStatus(false);
                    //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.you_are_logged_out), Toast.LENGTH_LONG).show();
                    if (logoutBackpress) {
                        logoutBackpress = false;
                        Intent intent = new Intent(context, HomeActivity.class);
                        TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).startActivities();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConfig> call, @NonNull Throwable t) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismissLoading(getBinding().progressBar);
    }

}
