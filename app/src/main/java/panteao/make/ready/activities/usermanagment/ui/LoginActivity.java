package panteao.make.ready.activities.usermanagment.ui;


import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
//import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
import panteao.make.ready.activities.purchase.callBack.EntitlementStatus;
import panteao.make.ready.activities.purchase.planslayer.GetPlansLayer;
import panteao.make.ready.activities.usermanagment.viewmodel.RegistrationLoginViewModel;
import panteao.make.ready.baseModels.BaseBindingActivity;
import panteao.make.ready.R;
import panteao.make.ready.beanModel.responseModels.LoginResponse.UserData;
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel;
import panteao.make.ready.databinding.ActivityMainBinding;
import panteao.make.ready.databinding.LoginBinding;
import panteao.make.ready.fragments.dialog.AlertDialogFragment;
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment;
import panteao.make.ready.tarcker.EventConstant;
import panteao.make.ready.tarcker.EventEnum;
import panteao.make.ready.tarcker.FCMEvents;
import panteao.make.ready.utils.commonMethods.AppCommonMethod;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import panteao.make.ready.utils.cropImage.helpers.NetworkConnectivity;
import panteao.make.ready.utils.cropImage.helpers.PrintLogging;
import panteao.make.ready.utils.helpers.AppPreference;
import panteao.make.ready.utils.helpers.CheckInternetConnection;

import panteao.make.ready.utils.helpers.StringUtils;
import panteao.make.ready.utils.helpers.ToastHandler;
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends BaseBindingActivity<LoginBinding> implements AlertDialogFragment.AlertDialogListener {
    private final List<String> permissionNeeds = Arrays.asList("email", "public_profile");
    boolean hasFbEmail;
    Bitmap bitmap;
    boolean isFbLoginClick = false;
    private KsPreferenceKeys preference;
    private RegistrationLoginViewModel viewModel ;
    private CallbackManager callbackManager;
    private String accessTokenFB;
    private String name = "", email = "", id = "";
    private ActivityMainBinding binding;
    private URL profile_pic;
    private String fbPIck;
    private Uri picUri;
    //   private TransferUtility transferUtility;
    private long mLastClickTime = 0;
    //   private AmazonS3 s3;
    private int counter = 0;
    private AsyncTask mMyTask;
    private UserData modelLogin;

    public static String getFileNameFromUrl(String url) {
        Logger.e("", "ProfilePic Name" + url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0]);
        Logger.e("", "ProfilePic path" + url);


        return url.substring(url.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    @Override
    public LoginBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return LoginBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI(getBinding().rootView);
        callBinding();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFbLoginClick = false;
        getBinding().llFooter.setVisibility(View.VISIBLE);
        dismissLoading(getBinding().progressBar);
        if (preference.getAppPrefLoginStatus()) {
            onBackPressed();
        }
    }

    private void callBinding() {
        viewModel = ViewModelProviders.of(LoginActivity.this).get(RegistrationLoginViewModel.class);
        getBinding().toolbar.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        connectObservors();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(LoginActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {

            connectObservors();
        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().rootView.setVisibility(View.GONE);
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.retryTxt.setOnClickListener(view -> {
            getBinding().rootView.setVisibility(View.VISIBLE);
            getBinding().noConnectionLayout.setVisibility(View.GONE);

            connectionObserver();
        });
    }

    String loginCallingFrom = "";

    public void connectObservors() {
        //intent.putExtra("loginFrom","home");
        loginCallingFrom = getIntent().getStringExtra("loginFrom");
//        credentialsProvider();
//        setTransferUtility();
        getBinding().signUpTxt.setClickable(true);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //  getBinding().fbButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        callbackManager = CallbackManager.Factory.create();
        preference = KsPreferenceKeys.getInstance();
        getBinding().fbButton.setReadPermissions(permissionNeeds);
        getBinding().errorEmail.setVisibility(View.INVISIBLE);
        getBinding().errorPassword.setVisibility(View.INVISIBLE);
        getBinding().etPassword.setLongClickable(false);
        getBinding().tvCancel.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            clearEditView();
            onBackPressed();
        });


        getBinding().etUserName.setOnTouchListener((view, motionEvent) -> {
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
            return false;
        });

        getBinding().etPassword.setOnTouchListener((view, motionEvent) -> {
            if (validateEmptyEmail() && validateEmail())
                getBinding().errorEmail.setVisibility(View.INVISIBLE);
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
            return false;
        });


        getBinding().etPassword.setOnClickListener(view -> {

            if (validateEmptyEmail() && validateEmail())
                getBinding().errorEmail.setVisibility(View.INVISIBLE);
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
        });


        getBinding().rlFacebookLogin.setOnClickListener(view -> {

            if (CheckInternetConnection.isOnline(LoginActivity.this)) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                clearEditView();
                isFbLoginClick = true;
                getBinding().fbButton.performClick();


            } else
                connectionObserver();
        });

        getBinding().fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
//                AppCommonMethod.trackFcmCustomEvent(getApplicationContext(),AppConstants.SIGN_IN_SUCCESS,"","","",0," ",0,"",0,0,"","",preference.getAppPrefUserId(),preference.getAppPrefUserName());

                accessTokenFB = loginResult.getAccessToken().getToken();
                Logger.i("accessToken", accessTokenFB);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {

                            Logger.i("LoginActivity",
                                    response.toString());
                            try {
                                id = object.getString("id");
                               /* try {
                                    profile_pic = new URL(
                                            "https://graph.facebook.com/" + id + "/picture?type=large");


                                    Logger.i("profile_pic",
                                            profile_pic + "");
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }*/
                                name = object.getString("name");
                                if (object.has("email")) {
                                    email = object.getString("email");
                                    hasFbEmail = true;
                                } else
                                    hasFbEmail = false;

                              /*  try {
                                    final URL imageUrl = new URL(
                                            "http://graph.facebook.com/" + id + "/picture?type=large");
                                    bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                                } catch (Exception e) {
                                    Logger.e("LoginActivity", "" + e.toString());

                                }*/
                                showHideProgress(getBinding().progressBar);
                                //  setFileToUpload();
                                hitApiFBLogin();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException)
                    LoginManager.getInstance().logOut();
               // Logger.e("LoginActivity", error.getCause().toString());
            }
        });


        getBinding().llLogin.setOnClickListener(view -> {


            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            callLogin();


         /* Random rand = new Random();
            int rand_int1 = rand.nextInt(1000);
            int rand_int2 = rand.nextInt(1000);
            new ActivityLauncher(LoginActivity.this).forceLogin(LoginActivity.this, ForceLoginFbActivity.class, "EAAJMIUJyU0IBAEIWKF4SXCkdvi7LNVY26L771ZCLAwobVxY3GkOd17teVvDTP1JK19YG6xQk643JwzGXrWibljsZCOkZCc1YTZARb0uSDasZANFOReNqIhAZBoWFuZClSC5OY9JZAG6rSzhCQv3x84ALsPURZCNCxGSwsiVzHeDZBNbzKN3WijMByEIH7GzM7NOhaZBinlkhxBsVA3qZAiqL"+rand_int1, rand_int2+"", "Ankur", "");*/

        });
        getBinding().etPassword.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (NetworkConnectivity.isOnline(LoginActivity.this)) {
                    getBinding().etPassword.setClickable(false);

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                        return true;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    callLogin();
                } else
                    new ToastHandler(LoginActivity.this).show(LoginActivity.this.getResources().getString(R.string.no_internet_connection));
            }

            return false;
        });


        getBinding().tvForgotPassword.setOnClickListener(view -> {
            showLoading(getBinding().progressBar, false);
            clearEditView();
            new ActivityLauncher(LoginActivity.this).forgotPasswordActivity(LoginActivity.this, ForgotPasswordActivity.class);

        });


        getBinding().signUpTxt.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            showLoading(getBinding().progressBar, false);
            clearEditView();
            finish();
            new ActivityLauncher(LoginActivity.this).signUpActivity(LoginActivity.this, SignUpActivity.class, loginCallingFrom);


        });
    }

    public void hitApiFBLogin() {
        if (CheckInternetConnection.isOnline(LoginActivity.this)) {

            showLoading(getBinding().progressBar, true);

            viewModel.hitFbLogin(LoginActivity.this, email, accessTokenFB, name, id, "", hasFbEmail).observe(LoginActivity.this, new Observer<LoginResponseModel>() {
                @Override
                public void onChanged(LoginResponseModel loginResponseModelResponse) {
                    if (Objects.requireNonNull(loginResponseModelResponse).getResponseCode() == 2000) {
                        Gson gson = new Gson();
                        modelLogin = loginResponseModelResponse.getData();
                        String stringJson = gson.toJson(loginResponseModelResponse.getData());
                        saveUserDetails(stringJson, loginResponseModelResponse.getData().getId(), false);
                AppCommonMethod.trackFcmCustomEvent(getApplicationContext(),AppConstants.SIGN_IN_SUCCESS,"","","",0," ",0,"",0,0,"","",loginResponseModelResponse.getData().getId()+"",loginResponseModelResponse.getData().getName()+"");

                    } else if (loginResponseModelResponse.getResponseCode() == 403) {
                        new ActivityLauncher(LoginActivity.this).forceLogin(LoginActivity.this, ForceLoginFbActivity.class, accessTokenFB, id, name, "");
                    } else {
                        dismissLoading(getBinding().progressBar);
                        showDialog(LoginActivity.this.getResources().getString(R.string.error), loginResponseModelResponse.getDebugMessage().toString());
                    }
                }
            });
        } else {
            new ToastHandler(LoginActivity.this).show(LoginActivity.this.getResources().getString(R.string.no_internet_connection));
        }
    }

    public void callLogin() {
        if (CheckInternetConnection.isOnline(LoginActivity.this)) {
            if (validateEmptyEmail() && validateEmail() && validateEmptyPassword() && passwordCheck(getBinding().etPassword.getText().toString())) {
                getBinding().errorEmail.setVisibility(View.INVISIBLE);
                getBinding().errorPassword.setVisibility(View.INVISIBLE);
                showLoading(getBinding().progressBar, true);
                viewModel.hitLoginAPI(LoginActivity.this, getBinding().etUserName.getText().toString(), getBinding().etPassword.getText().toString()).observe(LoginActivity.this, loginResponseModelResponse -> {
                    if (Objects.requireNonNull(loginResponseModelResponse).getResponseCode() == 2000) {
                        Gson gson = new Gson();
                        modelLogin = loginResponseModelResponse.getData();
                        String stringJson = gson.toJson(loginResponseModelResponse.getData());
                        saveUserDetails(stringJson, loginResponseModelResponse.getData().getId(), true);
                        Log.d("useridmanual",loginResponseModelResponse.getData().getId()+"");
                        Log.d("useridmanual",loginResponseModelResponse.getData().getName()+"");

                        AppCommonMethod.trackFcmCustomEvent(getApplicationContext(),AppConstants.SIGN_IN_SUCCESS,"","","",0," ",0,"",0,0,"","",loginResponseModelResponse.getData().getId()+"",loginResponseModelResponse.getData().getName()+"");

                    } else {
                        if (loginResponseModelResponse.getDebugMessage() != null) {
                            dismissLoading(getBinding().progressBar);
                            showDialog(LoginActivity.this.getResources().getString(R.string.error), loginResponseModelResponse.getDebugMessage().toString());
                        } else {
                            dismissLoading(getBinding().progressBar);
                            showDialog(LoginActivity.this.getResources().getString(R.string.error), LoginActivity.this.getResources().getString(R.string.something_went_wrong));

                        }
                    }
                    /*else if (loginResponseModelResponse.getResponseCode() == 401 || loginResponseModelResponse.getResponseCode() == 404) {
                        dismissLoading(getBinding().progressBar);
                        showDialog(LoginActivity.this.getResources().getString(R.string.error), loginResponseModelResponse.getDebugMessage().toString());
                    }
                    else if (loginResponseModelResponse.getResponseCode() == 500) {
                        dismissLoading(getBinding().progressBar);
                        showDialog(LoginActivity.this.getResources().getString(R.string.error), LoginActivity.this.getResources().getString(R.string.server_error));
                    }
                    else {
                        showDialog(LoginActivity.this.getResources().getString(R.string.error), loginResponseModelResponse.getDebugMessage().toString());
                        dismissLoading(getBinding().progressBar);
                    }*/

                });

            }
        } else {
            connectionObserver();
            // new ToastHandler(LoginActivity.this).show(LoginActivity.this.getResources().getString(R.string.no_internet_connection));

        }
    }

    public void saveUserDetails(String response, int userID, boolean isManual) {
        UserData fbLoginData = new Gson().fromJson(response, UserData.class);
        Gson gson = new Gson();
        String stringJson = gson.toJson(fbLoginData);
        Log.d("responsefb",stringJson);
        AppPreference.getInstance(this).clear();
        counter = 0;
        if (isManual)
            preference.setAppPrefLoginType(AppConstants.UserLoginType.Manual.toString());
        else
            preference.setAppPrefLoginType(AppConstants.UserLoginType.FbLogin.toString());
        preference.setAppPrefProfile(stringJson);
        preference.setAppPrefLoginStatus(true);
        preference.setAppPrefUserId(String.valueOf(fbLoginData.getId()));
        Log.d("fbuserid",preference.getAppPrefUserId());
        preference.setAppPrefUserName(String.valueOf(fbLoginData.getName()));
        preference.setAppPrefUserEmail(String.valueOf(fbLoginData.getEmail()));
        Log.d("fbuserid",preference.getAppPrefUserName());

        AppCommonMethod.userId = String.valueOf(fbLoginData.getId());
        String token = preference.getAppPrefAccessToken();
        if (loginCallingFrom.equalsIgnoreCase("home") && token != null && !token.equalsIgnoreCase("")) {
            GetPlansLayer.getInstance().getEntitlementStatus(preference, token, new EntitlementStatus() {
                @Override
                public void entitlementStatus(boolean entitlementStatus, boolean apiStatus) {
                    onBackPressed();
                }
            });
        } else {
            onBackPressed();
        }

        //new ActivityLauncher(LoginActivity.this).homeScreen(LoginActivity.this, HomeActivity.class);

        try {
            trackEvent(String.valueOf(fbLoginData.getName()), isManual);
        } catch (Exception e) {

        }
    }

    private void trackEvent(String name, boolean type) {
        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(EventConstant.Name, name);
        if (type) {
            requestParam.addProperty(EventConstant.PlatformType, EventEnum.Email.name());
        } else {
            requestParam.addProperty(EventConstant.PlatformType, EventEnum.Facebook.name());
        }

        FCMEvents.getInstance().setContext(LoginActivity.this).trackEvent(5, requestParam);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissLoading(getBinding().progressBar);
    }

    private boolean validateEmptyEmail() {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(getBinding().etUserName.getText().toString().trim())) {
            getBinding().errorEmail.setText(getResources().getString(R.string.empty_string));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        } else {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        }
        return check;
    }

    private void clearEditView() {
        getBinding().etUserName.setText("");
        getBinding().etPassword.setText("");
        getBinding().errorEmail.setVisibility(View.INVISIBLE);
        getBinding().errorPassword.setVisibility(View.INVISIBLE);
    }

    private boolean validateEmptyPassword() {
        boolean check = false;
        if (StringUtils.isNullOrEmptyOrZero(getBinding().etPassword.getText().toString().trim())) {
            getBinding().errorPassword.setText(getResources().getString(R.string.empty_password));
            getBinding().errorPassword.setVisibility(View.VISIBLE);
        } else {
            check = true;
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
        }

        return check;
    }

    public boolean validateEmail() {
        boolean check = false;
        if (getBinding().etUserName.getText().toString().trim().matches(AppConstants.EMAIL_REGEX)) {
            check = true;
            getBinding().errorEmail.setVisibility(View.INVISIBLE);
        } else {
            getBinding().errorEmail.setText(getResources().getString(R.string.valid_email));
            getBinding().errorEmail.setVisibility(View.VISIBLE);
        }
        return check;
    }

    public boolean passwordCheck(String password) {
        //String passwordRegex = "^(?=.*[!&^%$#@()\\_+-])[A-Za-z0-9\\d!&^%$#@()\\_+-]{8,20}$";
        String passwordRegex="^[A-Za-z0-9\\d!&^%$#@()\\_+-]{6,20}$";
        boolean check = false;
        Pattern mPattern = Pattern.compile(passwordRegex);
        Matcher matcher = mPattern.matcher(password.toString());
        if (!matcher.find()) {
            getBinding().errorPassword.setVisibility(View.VISIBLE);
            getBinding().errorPassword.setText(getResources().getString(R.string.strong_password_required));
            //showDialog(LoginActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.strong_password_required));
        } else {
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
            check = true;
        }

       /* if (getBinding().etPassword.getText().length() < 6) {
            getBinding().errorPassword.setVisibility(View.INVISIBLE);

            showDialog(LoginActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.strong_password_required));

        } else if (!stringContainsNumber(getBinding().etPassword.getText().toString())) {
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
            showDialog(LoginActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.credential_mismatch));

        } else {
            getBinding().errorPassword.setVisibility(View.INVISIBLE);
            check = true;
        }*/
        return check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Logger.e("LoginActivity", "" + e.toString());

        }
    }

    public boolean stringContainsNumber(String s) {
        return Pattern.compile("[0-9]").matcher(s).find();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();
    }

    private void showDialog(String title, String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(title, message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, "fragment_alert");
    }

//    private void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider) {
//        s3 = new AmazonS3Client(credentialsProvider);
//        s3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
//    }
//
//    public void setTransferUtility() {
//        transferUtility = new TransferUtility(s3, getApplicationContext());
//    }

//    private void transferObserverListener(TransferObserver transferObserver) {
//        LoginManager.getInstance().logOut();
//        transferObserver.setTransferListener(new TransferListener() {
//            @Override
//            public void onStateChanged(int id, TransferState state) {
//                if (state == TransferState.COMPLETED) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                    //profile_pic = transferObserver.getKey();
//                    hitApiFBLogin();
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

    @Override
    public void onFinishDialog() {


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFbLoginClick)
            getBinding().llFooter.setVisibility(View.GONE);
        getBinding().etPassword.setText("");
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

//    private void credentialsProvider() {
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                COGNITO_IDENTITY, Regions.AP_SOUTHEAST_1
//
//        );
//        setAmazonS3Client(credentialsProvider);
//    }

    public void setFileToUpload() {
        mMyTask = new DownloadTask()
                .execute(profile_pic);


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

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
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


                picUri = imageInternalUri;
//                TransferObserver transferObserver = transferUtility.upload(
//                        IMAGE_BUCKET_ADDRESS, getFileNameFromUrl(picUri.getPath()),
//                        new File(picUri.getPath())
//                );
//                transferObserverListener(transferObserver);
                PrintLogging.printLog("", "sdfhgdhjf" + imageInternalUri);
                // Set the ImageView image from internal storage
            } else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
