package panteao.make.ready.activities.profile.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Selection;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.AppUserModel;
import panteao.make.ready.beanModel.requestParamModel.RequestParamRegisterUser;
import panteao.make.ready.databinding.ProfileScreenBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.CheckInternetConnection;
import panteao.make.ready.utils.helpers.NetworkConnectivity;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.ToolBarHandler;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ProfileActivity extends BaseBindingActivity<ProfileScreenBinding> implements AlertDialogFragment.AlertDialogListener {
    private final List<String> permissionNeeds = Arrays.asList("email", "public_profile");
    boolean isPicFromUser = false;
    private RegistrationLoginViewModel viewModel;
    private RequestParamRegisterUser requestParamRegisterUser;
    private KsPreferenceKeys preference;
//    private AmazonS3 s3;
//    private TransferUtility transferUtility;
    private AppSyncBroadcast appSyncBroadcast;
    private long mLastClickTime = 0;
    private CallbackManager callbackManager;
    private String accessTokenFB;
    private String name = "", email = "", id = "";
    private URL fb_profile_pic_url;
    private String profilePicKey;
    private Uri picUri;
    private boolean isloggedout = false;
    private boolean hasProfilePic = false;
    private boolean isConnetWithFB = false;
    private AppUserModel userModel;

    public static String getFileNameFromUrl(String url) {
        Logger.e("", "ProfilePic Name" + url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0]);


        return url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasProfilePic = false;
        setupUI(getBinding().svProfile);
        new ToolBarHandler(this).profileAction(getBinding());
        callBinding();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(ProfileActivity.this).unregisterReceiver(appSyncBroadcast);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                Logger.i("TAG", "Tried to unregister the reciver when it's not registered");
            } else {
                throw e;
            }
        }
    }

    @Override
    public ProfileScreenBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ProfileScreenBinding.inflate(inflater);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isloggedout = false;
        isConnetWithFB = false;
        AppCommonMethod.getTimeStampDOB = 0;
        appSyncBroadcast = new AppSyncBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NONTON_PROFILE_UPDATE");
        LocalBroadcastManager.getInstance(ProfileActivity.this).registerReceiver(appSyncBroadcast, intentFilter);

    }

    private void callBinding() {
        viewModel = new ViewModelProvider(ProfileActivity.this).get(RegistrationLoginViewModel.class);
        connectionObserver();
    }

    public void checkInternet() {
        getBinding().rootView.setVisibility(View.VISIBLE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        connectWithFb();
        connectObservors();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(ProfileActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            checkInternet();
        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().rootView.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }

    public void connectWithFb() {
      //  FacebookSdk.sdkInitialize(this.getApplicationContext());
        getBinding().fbButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        callbackManager = CallbackManager.Factory.create();
        getBinding().fbButton.setPermissions(permissionNeeds);
        getBinding().tvConnectFb.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 800) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            // Toast.makeText(this, "hasFb" + hasProfilePic, Toast.LENGTH_LONG).show();
            isConnetWithFB = false;
            getBinding().fbButton.performClick();
        });


        getBinding().fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");

                accessTokenFB = loginResult.getAccessToken()
                        .getToken();
                Logger.i("accessToken", accessTokenFB);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {

                            Logger.i("LoginActivity",
                                    response.toString());
                            try {
                                id = object.getString("id");
                                try {
                                    fb_profile_pic_url = new URL(
                                            "https://graph.facebook.com/" + id + "/picture?type=large");
                                    Logger.i("profile_pic",
                                            fb_profile_pic_url + "");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                name = object.getString("name");
                                if (object.has("email")) {
                                    email = object.getString("email");
                                }
                                showLoading(getBinding().progressBar, false);
                                if (!StringUtils.isNullOrEmptyOrZero(userModel.getProfilePicURL()) && hasProfilePic) {
                                    try {
                                        hitApiConnectWithFB();
                                    } catch (Exception e) {
                                        Logger.e("", "" + e.toString());
                                    }
                                } else {
                                    downloadFacebookPic();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                try {
                    if (error instanceof FacebookAuthorizationException)
                        LoginManager.getInstance().logOut();
                    Logger.e("ProfileActivity", error.getCause().toString());
                } catch (Exception e) {
                    Logger.e("ProfileActivity", "" + e.toString());
                }
            }
        });
    }

    public void downloadFacebookPic() {
        LoginManager.getInstance().logOut();
        new DownloadTask()
                .execute(fb_profile_pic_url
                );

    }

    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images", MODE_PRIVATE);


        // Create a file to save the image
        file = new File(file, name + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }

    public void hitApiConnectWithFB() {
        String token = preference.getAppPrefAccessToken();
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_FB_ID, id);
        requestParam.addProperty(AppConstants.API_PARAM_NAME, name);
        requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email);
        requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, accessTokenFB);
        requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, true);


        if (!StringUtils.isNullOrEmptyOrZero(profilePicKey))
            requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, profilePicKey);
        else
            requestParam.add(AppConstants.API_PARAM_FB_PIC, JsonNull.INSTANCE);

        viewModel.hitApiConnectFb(ProfileActivity.this, token, requestParam).observe(ProfileActivity.this, jsonObject -> {

            LoginManager.getInstance().logOut();
            try {
                if (jsonObject.isStatus()) {
                    Logger.e("ProfileActivity", "" + jsonObject.isStatus());

                } else {
                    isConnetWithFB = false;
                    if (jsonObject.getResponseCode() == 400) {
                        showDialog(ProfileActivity.this.getResources().getString(R.string.error), jsonObject.getDebugMessage().toString());
                    }
                }
            } catch (Exception e) {

            }
            picUri = null;
            profilePicKey = "";
            dismissLoading(getBinding().progressBar);
        });


    }

    public void connectObservors() {
//        credentialsProvider();
//        setTransferUtility();

        userModel = AppUserModel.getInstance();
        if (!StringUtils.isNullOrEmptyOrZero(userModel.getProfilePicURL())) {
            hasProfilePic = true;
        }

        setUIData();
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());

        getBinding().editMobileNumber.setOnClickListener(view -> {
            getBinding().etProfileNumber.setEnabled(true);
            getBinding().etProfileNumber.requestFocus();
            int position = getBinding().etProfileNumber.getText().length();
            Editable etext = getBinding().etProfileNumber.getText();
            Selection.setSelection(etext, position);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(getBinding().etProfileNumber, InputMethodManager.SHOW_IMPLICIT);

        });

        getBinding().parentLayout.setOnClickListener(this::hideSoftKeyboard);

        getBinding().editName.setOnClickListener(view -> {
            getBinding().etProfileName.setEnabled(true);
            getBinding().etProfileName.requestFocus();

            int position = getBinding().etProfileName.getText().length();
            Editable etext = getBinding().etProfileName.getText();
            Selection.setSelection(etext, position);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(getBinding().etProfileName, InputMethodManager.SHOW_IMPLICIT);


        });


        getBinding().btnPhotoId.setOnClickListener(view -> uploadImage());
        getBinding().profileLayout.setOnClickListener(view -> uploadImage());

        getBinding().ivEditDOB.setOnClickListener(view -> {
            hideSoftKeyboard(view);
            ProfileActivity.this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            AppCommonMethod.showDatePickerNonton(getBinding().calendertxt, ProfileActivity.this);
        });

        getBinding().errorName.setOnTouchListener((view, motionEvent) -> {
            getBinding().errorName.setVisibility(View.INVISIBLE);
            return false;
        });

        getBinding().tvSave.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            hitSaveApi();
        });
    }

    public void hitSaveApi() {
        if (CheckInternetConnection.isOnline(ProfileActivity.this)) {
            getBinding().etProfileName.setEnabled(false);
            getBinding().etProfileNumber.setEnabled(false);
            if (StringUtils.isNullOrEmptyOrZero(getBinding().etProfileName.getText().toString())) {
                getBinding().errorName.setVisibility(View.VISIBLE);
                getBinding().errorName.setText(ProfileActivity.this.getResources().getString(R.string.name_validation_profile));
            } else {
                getBinding().errorName.setVisibility(View.INVISIBLE);
                //hit api to update
                if (setDataRequestModel()) {

                    showLoading(getBinding().progressBar, true);
                    if (picUri != null) {
                     //   setFileToUpload();
                    } else hitApiUploadData();


                }
            }

        } else
            new ToastHandler(ProfileActivity.this).show(ProfileActivity.this.getResources().getString(R.string.no_internet_connection));
    }

    public void uploadImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    public boolean setDataRequestModel() {

        requestParamRegisterUser = new RequestParamRegisterUser();
        requestParamRegisterUser.setId(userModel.getId());
        requestParamRegisterUser.setEmail(userModel.getEmail());
        requestParamRegisterUser.setName(getBinding().etProfileName.getText().toString());
        requestParamRegisterUser.setVerified(true);

        if (picUri != null) {
            requestParamRegisterUser.setProfilePicURL(new File(picUri.getPath()).toString());
            isPicFromUser = false;
        } else
            requestParamRegisterUser.setProfilePicURL(userModel.getProfilePicURL());

        if (getBinding().rbMale.isChecked())
            requestParamRegisterUser.setGender(AppConstants.GENDER_MALE);
        if (getBinding().rbFemale.isChecked())
            requestParamRegisterUser.setGender(AppConstants.GENDER_FEMALE);

        if (getBinding().etProfileNumber.getText().toString().isEmpty()) {
            preference.setAppPrefHasNumberEmpty(true);
        } else {
            preference.setAppPrefHasNumberEmpty(false);
            requestParamRegisterUser.setPhoneNumber(getBinding().etProfileNumber.getText().toString());
        }

        requestParamRegisterUser.setStatus(userModel.getStatus());
        requestParamRegisterUser.setProfileStep(AppConstants.PROFILE_SETUP);
        if (getBinding().calendertxt.getText().toString().isEmpty()) {
            preference.setAppPrefHasDOB(true);
        } else {
            preference.setAppPrefHasDOB(false);
            requestParamRegisterUser.setDateOfBirth(convertToMillisec(getBinding().calendertxt.getText().toString()));
        }
        String token = preference.getAppPrefAccessToken();
        if (!StringUtils.isNullOrEmptyOrZero(token))
            requestParamRegisterUser.setAccessToken(token);
        else
            Toast.makeText(ProfileActivity.this, "Token Not Available", Toast.LENGTH_LONG).show();
        return true;
    }

    public long convertToMillisec(String val) {
        ArrayList<String> dateConverted = AppCommonMethod.divideDate(val, "/");
        Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(dateConverted.get(2)), AppCommonMethod.getMonth(dateConverted.get(1)), Integer.valueOf(dateConverted.get(0)));
        return c.getTimeInMillis();
    }

    public void setUIData() {
        preference = KsPreferenceKeys.getInstance();

        getBinding().toolbar.llSearchIcon.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.screenText.setText(ProfileActivity.this.getResources().getString(R.string.my_profile));
        getBinding().etProfileName.setEnabled(false);
        getBinding().etProfileNumber.setEnabled(false);

        try {
            userModel = AppUserModel.getInstance();
            if (userModel == null) {
                String tempResponse = preference.getAppPrefUser();
                if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
                    userModel = new Gson().fromJson(tempResponse, AppUserModel.class);
                    setDataIntoUI();
                }
            } else {

                setDataIntoUI();
            }
        } catch (Exception e) {

        }


    }

    public void setDataIntoUI() {

        if (!userModel.isFbLinked()) {

            getBinding().tvConnectFb.setVisibility(View.VISIBLE);
        } else {
            getBinding().tvConnectFb.setVisibility(View.GONE);
        }
        getBinding().etProfileName.setText(userModel.getName());

        getBinding().tvUserEmail.setText(userModel.getEmail());

        if (StringUtils.isNullOrEmpty(userModel.getDateOfBirth())) {
            getBinding().calendertxt.getText().clear();
        } else {
            long dob = Long.parseLong(userModel.getDateOfBirth());
            if (dob == 0) {
                Logger.e("ProfileActivity", "dob is zero");

            } else {
                getBinding().calendertxt.setText(getDate(dob));
            }
        }
        if (StringUtils.isNullOrEmpty(userModel.getPhoneNumber()))
            getBinding().etProfileNumber.getText().clear();
        else
            getBinding().etProfileNumber.setText(userModel.getPhoneNumber());
        if (!StringUtils.isNullOrEmpty(userModel.getGender()) && userModel.getGender().equalsIgnoreCase("Male"))
            getBinding().rbMale.setChecked(true);
        if (!StringUtils.isNullOrEmpty(userModel.getGender()) && userModel.getGender().equalsIgnoreCase("Female"))
            getBinding().rbFemale.setChecked(true);
        if (!StringUtils.isNullOrEmpty(userModel.getProfilePicURL()))
            if (userModel.getProfilePicURL().length() > 0) {
                try {
                    profilePicKey = userModel.getProfilePicURL();
                    setProfilePic(userModel.getProfilePicURL());
                    Logger.e("imageURL", "" + userModel.getProfilePicURL());
                } catch (Exception e) {
                    Logger.e("", "" + e.toString());
                }
            } else {
                Glide.with(this).load(ProfileActivity.this.getResources().getDrawable(R.drawable.default_profile_pic)).into(getBinding().ivProfilePic);
            }

    }

    public void setProfilePic(String key) {
        StringBuilder stringBuilder = new StringBuilder();
        String url1 = preference.getAppPrefCfep();
        if (key.contains("http")) {
            stringBuilder.append(url1).append("/fit-in/160x90/filters:quality(100):max_bytes(400)/").append(key);
        } else {
            if (StringUtils.isNullOrEmpty(url1)) {
                url1 = AppCommonMethod.urlPoints;
                preference.setAppPrefCfep(url1);
            }
            String url2 = AppConstants.PROFILE_FOLDER;
            stringBuilder.append(url1).append(url2).append(key);
        }

        picUri = null;
        Glide.with(ProfileActivity.this)
                .asBitmap()
                .load(stringBuilder.toString())
                .apply(AppCommonMethod.options)
                .into(getBinding().ivProfilePic);
        hasProfilePic = true;

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String monthname = (String) android.text.format.DateFormat.format("MMM", cal);
        String monthNumber = (String) DateFormat.format("dd", cal);
        String year = (String) DateFormat.format("yyyy", cal);
        if (monthNumber.length() < 2) {
            monthNumber = "0" + monthNumber;
        }
        return monthNumber + "/" + monthname + "/" + year;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        picUri = resultUri;
                        isPicFromUser = true;
                        Glide.with(this).load(resultUri).into(getBinding().ivProfilePic);
                        //From here you can load the image however you need to, I recommend using the Glide library
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Logger.e("resultCode", "" + result.getError());
                        picUri = null;
                    }
                    break;

            }

        }
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


//    public void setTransferUtility() {
//        transferUtility = new TransferUtility(s3, getApplicationContext());
//    }

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
//                    profilePicKey = transferObserver.getKey();
//                    if (isConnetWithFB) {
//                        hitApiConnectWithFB();
//                    } else {
//                        requestParamRegisterUser.setProfilePicURL(profilePicKey);
//                        hitApiUploadData();
//                    }
//
//
//                } else if (state == TransferState.FAILED) {
//                    getBinding().progressBar.setVisibility(View.GONE);
//                    showDialog(ProfileActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.image_upload_fail));
//                }
//
//
//            }
//
//            @Override
//            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                // int progress = (int) ((double) bytesCurrent * 100 / bytesTotal);
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

    private void hitApiUploadData() {
        viewModel.hitRegisterSignUpAPI(ProfileActivity.this, requestParamRegisterUser).observe(ProfileActivity.this, signUpResponseModel -> {
            dismissLoading(getBinding().progressBar);
            if (signUpResponseModel.isStatus()) {
                showDialog(ProfileActivity.this.getResources().getString(R.string.profile), getResources().getString(R.string.profile_update_successfully));
            } else {
                if (signUpResponseModel.getResponseCode() == 401) {
                    isloggedout = true;
                    showDialog(ProfileActivity.this.getResources().getString(R.string.profile), getResources().getString(R.string.you_are_logged_out));
                } else {
                    showDialog(ProfileActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                }
            }
        });

    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFinishDialog() {
        if (isloggedout) {
            hitApiLogout(ProfileActivity.this, preference.getAppPrefAccessToken());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismissLoading(getBinding().progressBar);
    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start

        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;

            try {
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog

            if (result != null) {


                // Display the downloaded image into ImageView

                // Save bitmap to internal storage
                Uri imageInternalUri = saveImageToInternalStorage(result);
                isConnetWithFB = true;
                picUri = imageInternalUri;
//                TransferObserver transferObserver = transferUtility.upload(
//                        IMAGE_BUCKET_ADDRESS, getFileNameFromUrl(picUri.getPath()),
//                        new File(picUri.getPath())
//                );
//                transferObserverListener(transferObserver);
                PrintLogging.printLog("", "DownloadTask" + imageInternalUri);
                // Set the ImageView image from internal storage
            } else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public class AppSyncBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUIData();
        }
    }
}
