package panteao.make.ready.repository.purchase
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.make.baseCollection.baseCategoryServices.BaseCategoryServices
import com.make.userManagement.callBacks.ForgotPasswordCallBack
import com.make.userManagement.callBacks.LoginCallBack
import com.make.userManagement.callBacks.MyPurchasesCallBack
import com.make.userManagement.callBacks.UserProfileCallBack
import org.json.JSONObject
import panteao.make.ready.PanteaoApplication
import panteao.make.ready.R
import panteao.make.ready.beanModel.connectFb.ResponseConnectFb
import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse
import panteao.make.ready.beanModel.facebook.loginstatusresponse.LoginStatusResponse
import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse
import panteao.make.ready.beanModel.forgotPassword.CommonResponse
import panteao.make.ready.beanModel.requestParamModel.RequestParamRegisterUser
import panteao.make.ready.beanModel.responseModels.LoginResponse.LoginResponseModel
import panteao.make.ready.beanModel.responseModels.MyPurchasesResponseModel
import panteao.make.ready.beanModel.responseModels.RegisterSignUpModels.ResponseRegisteredSignup
import panteao.make.ready.beanModel.responseModels.SignUp.SignupResponseAccessToken
import panteao.make.ready.beanModel.userProfile.UserProfileResponse
import panteao.make.ready.callbacks.facebookcallbacks.FacebookLoginCallback
import panteao.make.ready.callbacks.facebookcallbacks.FacebookLoginStatusCallback
import panteao.make.ready.callbacks.facebookcallbacks.FacebookUserProfileCallback
import panteao.make.ready.networking.apiendpoints.ApiInterface
import panteao.make.ready.networking.apiendpoints.RequestConfig
import panteao.make.ready.networking.intercepter.ErrorCodesIntercepter
import panteao.make.ready.repository.detail.DetailRepository
import panteao.make.ready.repository.userManagement.FacebookLoginRepository
import panteao.make.ready.repository.userManagement.FbLoginRequest
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.StringUtils
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MyPurchasesRepository private constructor() {

    companion object{
        private var myPurchaseRepository: MyPurchasesRepository?=null
        @Synchronized
        fun getInstance(): MyPurchasesRepository? {
            if (myPurchaseRepository == null) {
                myPurchaseRepository = MyPurchasesRepository()
            }
            return myPurchaseRepository
        }
    }
    fun getMyPurchasesAPIresponse(context: Context?, auth:String?,page:String?, size:String?, orderStatus:String?=""): LiveData<MyPurchasesResponseModel> {
        val responseApi: MutableLiveData<MyPurchasesResponseModel> = MutableLiveData()
        val requestParam = JsonObject()
        requestParam.addProperty(AppConstants.API_PARAM_PAGE, page)
        requestParam.addProperty(AppConstants.API_PARAM_SIZE, 100)
        requestParam.addProperty(AppConstants.API_PARAM_ORDER_STATUS, orderStatus)
        BaseCategoryServices.instance.mypurchasesService(auth!!,page!!, size!!, object :
            MyPurchasesCallBack {
            override fun success(
                status: Boolean,
                response: Response<MyPurchasesResponseModel>
            ) {
                if (status) {
                    var cl: MyPurchasesResponseModel
                    if (response.body() != null) {
                        /*val token = response.headers()["x-auth"]
                        val preference = KsPreferenceKeys.getInstance()
                        preference.appPrefAccessToken = token
                        val gson = Gson()
                        val tmp = gson.toJson(response.body())
                        val loginItemBean = gson.fromJson(tmp, MyPurchasesResponseModel::class.java)*/
                        val gson = Gson()
                        val tmp = gson.toJson(response.body())
                        val loginItemBean = gson.fromJson(tmp, MyPurchasesResponseModel::class.java)
                        loginItemBean.isSuccessful=true
                        responseApi.postValue(loginItemBean)
                    } else {
                        val responseModel = ErrorCodesIntercepter.getInstance().MyPurchase(response)
                        responseApi.postValue(responseModel)
                    }
                }
            }

            override fun failure(status: Boolean, errorCode: Int, errorMessage: String) {
                Logger.e("", "MyPurchaseResponseModel E$errorMessage")
                val cl = MyPurchasesResponseModel()
                cl.isSuccessful = false
                responseApi.postValue(cl)
            }
        })
        return responseApi
    }

//    fun getSignupAPIResponse(
//        name: String?,
//        email: String?,
//        pwd: String?
//    ): LiveData<SignupResponseAccessToken> {
//        val responseApi: MutableLiveData<SignupResponseAccessToken>
//        run {
//            responseApi = MutableLiveData()
//            val endpoint = RequestConfig.getClient().create(ApiInterface::class.java)
//            val requestParam = JsonObject()
//            requestParam.addProperty(AppConstants.API_PARAM_NAME, "3221312@#@#@##!#!")
//            requestParam.addProperty(AppConstants.API_PARAM_EMAIL, "")
//            requestParam.addProperty(AppConstants.API_PARAM_PASSWORD, pwd)
//            BaseCategoryServices.instance.registerService(
//                name!!,
//                email!!,
//                pwd!!,
//                object : LoginCallBack {
//                    override fun success(
//                        status: Boolean,
//                        response: Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel>
//                    ) {
//                        if (status) {
//                            try {
//                                if (response != null && response.errorBody() == null) {
//                                    Log.w("responseValue", response.code().toString() + "")
//                                }
//                            } catch (e: Exception) {
//                            }
//                            if (response.code() == 200 && response.body() != null) {
//                                val gson = Gson()
//                                val tmp = gson.toJson(response.body())
//                                val cl = gson.fromJson(tmp, LoginResponseModel::class.java)
//                                cl.responseCode = 200
//                                val token = response.headers()["x-auth"]
//                                val responseModel = SignupResponseAccessToken()
//                                responseModel.accessToken = token
//                                responseModel.responseModel = cl
//                                Logger.e("manual", "nNontonToken$token")
//                                responseApi.postValue(responseModel)
//                                Logger.e("", "REsponse" + response.body())
//                            } else {
//                                val responseModel =
//                                    ErrorCodesIntercepter.getInstance().manualSignUp(response)
//                                responseApi.postValue(responseModel)
//                            }
//                        }
//                    }
//
//                    override fun failure(status: Boolean, errorCode: Int, errorMessage: String) {
//                        Logger.e("", "LoginResponseToUI E$errorMessage")
//                        val cl = LoginResponseModel()
//                        cl.responseCode = 400
//                        val responseModel = SignupResponseAccessToken()
//                        responseModel.responseModel = cl
//                        if (KsPreferenceKeys.getInstance().appLanguage.equals(
//                                "English",
//                                ignoreCase = true
//                            )
//                        ) {
//                            responseModel.setDebugMessage(
//                                PanteaoApplication.getInstance().resources.getString(
//                                    R.string.server_error
//                                )
//                            )
//                        } else {
//                            responseModel.setDebugMessage(
//                                PanteaoApplication.getInstance().resources.getString(
//                                    R.string.server_error
//                                )
//                            )
//                        }
//                        responseApi.postValue(responseModel)
//                    }
//                })
//        }
//        return responseApi
//    }
//
//    fun getSignupAPIResponse(
//        context: Context?,
//        userDetails: RequestParamRegisterUser
//    ): LiveData<ResponseRegisteredSignup?> {
//        val responseApi: MutableLiveData<ResponseRegisteredSignup?>
//        responseApi = MutableLiveData()
//        var check: Boolean
//        val preference = KsPreferenceKeys.getInstance()
//        Logger.e("Token", "nNontonToken" + userDetails.accessToken)
//        val endpoint = RequestConfig.getClientInterceptor(userDetails.accessToken).create(
//            ApiInterface::class.java
//        )
//        val requestParam = JsonObject()
//        requestParam.addProperty(AppConstants.API_PARAM_NAME, userDetails.name)
//        requestParam.addProperty(AppConstants.API_PARAM_IS_VERIFIED, userDetails.isVerified)
//        requestParam.addProperty(
//            AppConstants.API_PARAM_VERIFICATION_DATE,
//            userDetails.verificationDate
//        )
//        if (StringUtils.isNullOrEmptyOrZero(userDetails.profilePicURL)) {
//            requestParam.add(AppConstants.API_PARAM_PROFILE_PIC, JsonNull.INSTANCE)
//        } else {
//            requestParam.addProperty(AppConstants.API_PARAM_PROFILE_PIC, userDetails.profilePicURL)
//        }
//        check = preference.appPrefDOB
//        if (check) requestParam.add(
//            AppConstants.API_PARAM_DOB,
//            JsonNull.INSTANCE
//        ) else requestParam.addProperty(
//            AppConstants.API_PARAM_DOB, userDetails.dateOfBirth
//        )
//        check = preference.appPrefHasNumberEmpty
//        if (check) requestParam.add(
//            AppConstants.API_PARAM_PHONE_NUMBER,
//            JsonNull.INSTANCE
//        ) else requestParam.addProperty(
//            AppConstants.API_PARAM_PHONE_NUMBER, userDetails.phoneNumber.toString()
//        )
//        requestParam.addProperty(AppConstants.API_PARAM_STATUS, userDetails.status)
//        requestParam.addProperty(AppConstants.API_PARAM_EXPIRY_DATE, userDetails.expiryDate)
//        requestParam.addProperty(AppConstants.API_PARAM_GENDER, userDetails.gender)
//        requestParam.addProperty(AppConstants.API_PARAM_PROFILE_STEP, "STEP_2")
//        val call = endpoint.getRegistrationStep(requestParam)
//        call.enqueue(object : Callback<ResponseRegisteredSignup?> {
//            override fun onResponse(
//                call: Call<ResponseRegisteredSignup?>,
//                response: Response<ResponseRegisteredSignup?>
//            ) {
//                // SignUpResponseModel cl = response.body();
//                if (response.code() == 200) {
//                    val temp = response.body()
//                    temp!!.isStatus = true
//                    temp.responseCode = response.code()
//                    responseApi.postValue(response.body())
//                } else if (response.code() == 401) {
//                    val temp = ResponseRegisteredSignup()
//                    temp.responseCode = response.code()
//                    temp.isStatus = false
//                    responseApi.postValue(temp)
//                } else {
//                    val temp = ResponseRegisteredSignup()
//                    temp.responseCode = Objects.requireNonNull(response.code())
//                    temp.isStatus = false
//                    responseApi.postValue(temp)
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseRegisteredSignup?>, t: Throwable) {
//                Logger.e("error", "REsponse$call")
//                try {
//                    if (call.execute().body() != null) responseApi.postValue(
//                        call.execute().body()
//                    ) else {
//                        val temp = ResponseRegisteredSignup()
//                        temp.isStatus = false
//                        temp.responseCode = 500
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        })
//        return responseApi
//    }
//
//    fun getForgotPasswordAPIResponse(email: String?): LiveData<CommonResponse> {
//        val responseApi: MutableLiveData<CommonResponse>
//        run {
//            val commonResponse = CommonResponse()
//            responseApi = MutableLiveData()
//            BaseCategoryServices.instance.forgotPasswordService(
//                email!!,
//                object : ForgotPasswordCallBack {
//                    override fun success(status: Boolean, response: Response<JsonObject>) {
//                        if (response.code() == 200) {
//                            commonResponse.code = response.code()
//                            responseApi.postValue(commonResponse)
//                        } else {
//                            var debugMessage = ""
//                            try {
//                                val jObjError = JSONObject(response.errorBody()!!.string())
//                                debugMessage = jObjError.getString("debugMessage")
//                                val errorcode = jObjError.getInt("responseCode")
//                                if (errorcode == 4401) {
//                                    commonResponse.debugMessage = PanteaoApplication.getInstance()
//                                        .getString(R.string.user_does_not_exists)
//                                    commonResponse.code = response.code()
//                                } else {
//                                    commonResponse.debugMessage = debugMessage
//                                    commonResponse.code = response.code()
//                                }
//                                Logger.e("", "" + jObjError.getString("debugMessage"))
//                            } catch (e: Exception) {
//                                Logger.e("RegistrationLoginRepo", "" + e.toString())
//                            }
//                            responseApi.postValue(commonResponse)
//                        }
//                    }
//
//                    override fun failure(status: Boolean, errorCode: Int, message: String) {
//                        commonResponse.debugMessage = ""
//                        commonResponse.code = 500
//                        responseApi.postValue(commonResponse)
//                    }
//                })
//        }
//        return responseApi
//    }
//
//    fun getChangePwdAPIResponse(
//        pwd: String?,
//        token: String?,
//        context: Context?
//    ): LiveData<LoginResponseModel> {
//        val responseApi: MutableLiveData<LoginResponseModel>
//        responseApi = MutableLiveData()
//        val requestParam = JsonObject()
//        requestParam.addProperty(AppConstants.API_PARAM_NEW_PWD, pwd)
//        BaseCategoryServices.instance.changePasswordService(
//            requestParam,
//            token!!,
//            object : LoginCallBack {
//                override fun success(
//                    status: Boolean,
//                    response: Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel>
//                ) {
//                    if (status) {
//                        val cl: LoginResponseModel
//                        if (response.code() == 500) {
//                            cl = LoginResponseModel()
//                            cl.responseCode = Objects.requireNonNull(response.code())
//                            responseApi.postValue(cl)
//                        } else if (response.code() == 401 || response.code() == 404) {
//                            cl = LoginResponseModel()
//                            cl.responseCode = response.code()
//                            var debugMessage = ""
//                            try {
//                                val jObjError = JSONObject(response.errorBody()!!.string())
//                                debugMessage = jObjError.getString("debugMessage")
//                                Logger.e("", "" + jObjError.getString("debugMessage"))
//                            } catch (e: Exception) {
//                                Logger.e("RegistrationLoginRepo", "" + e.toString())
//                            }
//                            cl.setDebugMessage(debugMessage)
//                            responseApi.postValue(cl)
//                        } else if (response.code() == 403) {
//                            cl = LoginResponseModel()
//                            cl.responseCode = response.code()
//                            var debugMessage = ""
//                            try {
//                                val jObjError = JSONObject(response.errorBody()!!.string())
//                                debugMessage = jObjError.getString("debugMessage")
//                                Logger.e("", "" + jObjError.getString("debugMessage"))
//                            } catch (e: Exception) {
//                                Logger.e("RegistrationLoginRepo", "" + e.toString())
//                            }
//                            if (KsPreferenceKeys.getInstance().appLanguage.equals(
//                                    "English",
//                                    ignoreCase = true
//                                )
//                            ) {
//                                cl.setDebugMessage(
//                                    PanteaoApplication.getInstance().resources.getString(
//                                        R.string.username_must_be_loggedin
//                                    )
//                                )
//                            } else {
//                                cl.setDebugMessage(
//                                    PanteaoApplication.getInstance().resources.getString(
//                                        R.string.username_must_be_loggedin
//                                    )
//                                )
//                            }
//                            responseApi.postValue(cl)
//                        } else if (response.body() != null) {
//                            Logger.e("", "LoginResponseModel" + response.body())
//                            val token = response.headers()["x-auth"]
//                            val preference = KsPreferenceKeys.getInstance()
//                            preference.appPrefAccessToken = token
//                            val gson = Gson()
//                            val tmp = gson.toJson(response.body())
//                            val loginItemBean = gson.fromJson(tmp, LoginResponseModel::class.java)
//                            responseApi.postValue(loginItemBean)
//                        }
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, errorMessage: String) {
//                    Logger.e("", "LoginResponseToUI E$errorMessage")
//                    val cl = LoginResponseModel()
//                    cl.isStatus = false
//                    responseApi.postValue(cl)
//                }
//            })
//
//        /* call.enqueue(new Callback<ResponseChangePassword>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseChangePassword> call, @NonNull ContinueWatchingModel<ResponseChangePassword> response) {
//                    if (response.code() == 200) {
//                        ResponseChangePassword model = response.body();
//                        model.setAccessToken(response.headers().get("x-auth"));
//                        model.setStatus(true);
//                        responseApi.postValue(response.body());
//                    } else {
//                        ResponseChangePassword model = new ResponseChangePassword();
//                        model.setStatus(false);
//                        model.setResponseCode(Objects.requireNonNull(response.code()));
//                        responseApi.postValue(model);
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseChangePassword> call, @NonNull Throwable t) {
//                    ResponseChangePassword model = new ResponseChangePassword();
//                    model.setStatus(false);
//                    responseApi.postValue(model);
//
//                }
//            });*/return responseApi
//    }
//
//    fun getFbLogin(
//        context: Context?,
//        email: String?,
//        fbToken: String?,
//        name: String?,
//        fbId: String?,
//        profilePic: String?,
//        isEmail: Boolean
//    ): LiveData<LoginResponseModel?> {
//        val responseApi: MutableLiveData<LoginResponseModel?>
//        run {
//            responseApi = MutableLiveData()
//            val endpoint = RequestConfig.getEnveuSubscriptionClient().create(
//                ApiInterface::class.java
//            )
//            val requestParam = JsonObject()
//            requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbId)
//            requestParam.addProperty(AppConstants.API_PARAM_NAME, name)
//            requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email)
//            requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbToken)
//            requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, isEmail)
//            requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, profilePic)
//            val call = endpoint.getFbLogin(requestParam)
//            BaseCategoryServices.instance.fbLoginService(requestParam, object : LoginCallBack {
//                override fun success(
//                    status: Boolean,
//                    response: Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel>
//                ) {
//                    if (status) {
//                        var cl: LoginResponseModel
//                        if (response.body() != null) {
//                            val token = response.headers()["x-auth"]
//                            val preference = KsPreferenceKeys.getInstance()
//                            preference.appPrefAccessToken = token
//                            val gson = Gson()
//                            val tmp = gson.toJson(response.body())
//                            val loginItemBean = gson.fromJson(tmp, LoginResponseModel::class.java)
//                            responseApi.postValue(loginItemBean)
//                        } else {
//                            val responseModel =
//                                ErrorCodesIntercepter.getInstance().fbLogin(response)
//                            responseApi.postValue(responseModel)
//                        }
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, message: String) {
//                    Logger.e("ResponseError", "getFbLogin$call")
//                    try {
//                        responseApi.postValue(call.execute().body())
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            })
//        }
//        return responseApi
//    }
//
//    fun getForceFbLogin(
//        context: Context,
//        email: String?,
//        fbToken: String?,
//        name: String?,
//        fbId: String?,
//        profilePic: String?,
//        isEmail: Boolean
//    ): LiveData<LoginResponseModel?> {
//        val responseApi: MutableLiveData<LoginResponseModel?>
//        run {
//            responseApi = MutableLiveData()
//            val endpoint = RequestConfig.getEnveuSubscriptionClient().create(
//                ApiInterface::class.java
//            )
//            val requestParam = JsonObject()
//            requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbId)
//            requestParam.addProperty(AppConstants.API_PARAM_NAME, name)
//            requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email)
//            requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbToken)
//            requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, isEmail)
//            requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, profilePic)
//            val call = endpoint.getForceFbLogin(requestParam)
//            BaseCategoryServices.instance.fbForceLoginService(requestParam, object : LoginCallBack {
//                override fun success(
//                    status: Boolean,
//                    response: Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel>
//                ) {
//                    val cl: LoginResponseModel
//                    if (status) {
//                        if (response.body() != null) {
//                            Logger.e("", "LoginResponseModel" + response.body())
//                            val token = response.headers()["x-auth"]
//                            val preference = KsPreferenceKeys.getInstance()
//                            preference.appPrefAccessToken = token
//                            val gson = Gson()
//                            val tmp = gson.toJson(response.body())
//                            val loginItemBean = gson.fromJson(tmp, LoginResponseModel::class.java)
//                            responseApi.postValue(loginItemBean)
//                        } else {
//                            val responseModel =
//                                ErrorCodesIntercepter.getInstance().fbLogin(response)
//                            responseApi.postValue(responseModel)
//                        }
//                    } else {
//                        cl = LoginResponseModel()
//                        cl.responseCode = response.code()
//                        val debugMessage = context.resources.getString(R.string.server_error)
//                        cl.setDebugMessage(debugMessage)
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, message: String) {
//                    Logger.e("ResponseError", "getFbLogin$call")
//                    try {
//                        responseApi.postValue(call.execute().body())
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            })
//        }
//        return responseApi
//    }
//
//    fun getConnectFb(
//        context: Context?,
//        token: String?,
//        requestParam: JsonObject?
//    ): LiveData<ResponseConnectFb> {
//        val responseApi: MutableLiveData<ResponseConnectFb>
//        run {
//            responseApi = MutableLiveData()
//            val endpoint = RequestConfig.getClientInterceptor(token).create(
//                ApiInterface::class.java
//            )
//            endpoint.getConnectFb(requestParam).enqueue(object : Callback<ResponseConnectFb> {
//                override fun onResponse(
//                    call: Call<ResponseConnectFb>,
//                    response: Response<ResponseConnectFb>
//                ) {
//                    val model = ResponseConnectFb()
//                    if (response.code() == 200) {
//                        model.isStatus = true
//                        model.data = response.body()!!.data
//                        responseApi.postValue(model)
//                        val preference = KsPreferenceKeys.getInstance()
//                        preference.appPrefAccessToken = response.headers()["x-auth-token"]
//                    } else if (response.code() == 400) {
//                        val debugMessage: String
//                        var code = 0
//                        try {
//                            val jObjError = JSONObject(response.errorBody()!!.string())
//                            debugMessage = jObjError.getString("debugMessage")
//                            code = jObjError.getInt("responseCode")
//                            model.responseCode = 400
//                            model.debugMessage = debugMessage
//                            model.isStatus = false
//                            responseApi.postValue(model)
//                            Logger.e("", "" + jObjError.getString("debugMessage"))
//                        } catch (e: Exception) {
//                            Logger.e("RegistrationLoginRepo", "" + e.toString())
//                        }
//                    } else {
//                        model.isStatus = false
//                        responseApi.postValue(model)
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseConnectFb>, t: Throwable) {
//                    val model = ResponseConnectFb()
//                    model.isStatus = false
//                }
//            })
//            return responseApi
//        }
//    }
//
//    fun hitApiLogout(session: Boolean, token: String?): LiveData<JsonObject?> {
//        val responseApi: MutableLiveData<JsonObject?>
//        run {
//            responseApi = MutableLiveData()
//            val endpoint = RequestConfig.getClientInterceptor(token).create(
//                ApiInterface::class.java
//            )
//            val call = endpoint.getLogout(session)
//            call.enqueue(object : Callback<JsonObject?> {
//                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                    try {
//                    } catch (e: Exception) {
//                    }
//                    if (response.code() == 404) {
//                        val jsonObject = JsonObject()
//                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                        responseApi.postValue(jsonObject)
//                    } else if (response.code() == 200) {
//                        Objects.requireNonNull(response.body())!!
//                            .addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                        responseApi.postValue(response.body())
//                    } else if (response.code() == 401) {
//                        val jsonObject = JsonObject()
//                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                        responseApi.postValue(jsonObject)
//                    } else if (response.code() == 500) {
//                        val jsonObject = JsonObject()
//                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                        responseApi.postValue(jsonObject)
//                    }
//                }
//
//                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {}
//            })
//        }
//        return responseApi
//    }
//
//    fun getUserProfile(context: Context?, token: String?): LiveData<UserProfileResponse> {
//        val mutableLiveData = MutableLiveData<UserProfileResponse>()
//        BaseCategoryServices.instance.userProfileService(token!!, object : UserProfileCallBack {
//            override fun success(
//                status: Boolean,
//                response: Response<com.make.userManagement.bean.UserProfile.UserProfileResponse>
//            ) {
//                val cl: UserProfileResponse
//                if (status) {
//                    if (response != null) {
//                        if (response.code() == 200) {
//                            val gson = Gson()
//                            val tmp = gson.toJson(response.body())
//                            val profileItemBean =
//                                gson.fromJson(tmp, UserProfileResponse::class.java)
//                            profileItemBean.status = true
//                            mutableLiveData.postValue(profileItemBean)
//                        } else {
//                            cl = ErrorCodesIntercepter.getInstance().userProfile(response)
//                            mutableLiveData.postValue(cl)
//                        }
//                    }
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                val cl = UserProfileResponse()
//                cl.status = false
//                mutableLiveData.postValue(cl)
//            }
//        })
//        return mutableLiveData
//    }
//
//    fun getUpdateProfile(
//        context: Context?,
//        token: String?,
//        name: String?
//    ): LiveData<UserProfileResponse> {
//        val mutableLiveData = MutableLiveData<UserProfileResponse>()
//        BaseCategoryServices.instance.userUpdateProfileService(
//            token!!,
//            name!!,
//            object : UserProfileCallBack {
//                override fun success(
//                    status: Boolean,
//                    response: Response<com.make.userManagement.bean.UserProfile.UserProfileResponse>
//                ) {
//                    val cl: UserProfileResponse
//                    if (status) {
//                        if (response != null) {
//                            if (response.code() == 200) {
//                                val gson = Gson()
//                                val tmp = gson.toJson(response.body())
//                                val profileItemBean =
//                                    gson.fromJson(tmp, UserProfileResponse::class.java)
//                                profileItemBean.status = true
//                                mutableLiveData.postValue(profileItemBean)
//                            } else {
//                                cl = ErrorCodesIntercepter.getInstance().userProfile(response)
//                                mutableLiveData.postValue(cl)
//                            }
//                        }
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, message: String) {
//                    val cl = UserProfileResponse()
//                    cl.status = false
//                    mutableLiveData.postValue(cl)
//                }
//            })
//        return mutableLiveData
//    }
//
//    companion object {
//        @JvmStatic
//        var instance: MyPurchasesRepository? = null
//            get() {
//                if (field == null) {
//                    field = MyPurchasesRepository()
//                }
//                if (KsPreferenceKeys.getInstance().appLanguage.equals(
//                        "Thai",
//                        ignoreCase = true
//                    ) || KsPreferenceKeys.getInstance().appLanguage.equals(
//                        "हिंदी",
//                        ignoreCase = true
//                    )
//                ) {
//                    AppCommonMethod.updateLanguage("th", PanteaoApplication.getInstance())
//                } else if (KsPreferenceKeys.getInstance().appLanguage.equals(
//                        "English",
//                        ignoreCase = true
//                    )
//                ) {
//                    AppCommonMethod.updateLanguage("en", PanteaoApplication.getInstance())
//                }
//                return field
//            }
//            private set
//    }
//
//    fun facebookLoginApi(
//        context: Context,
//        accessToken: String,
//        scope: String
//    ): MutableLiveData<FacebookLoginResponse> {
//        val connection = MutableLiveData<FacebookLoginResponse>()
//        FacebookLoginRepository.instance.facebookLoginApi(
//            accessToken,
//            scope,
//            object : FacebookLoginCallback {
//                override fun configurationSuccess(facebookLoginResponse: FacebookLoginResponse?) {
//                    connection.postValue(facebookLoginResponse)
//                }
//
//                override fun configurationFailure(message: String) {
//                }
//            })
//        return connection
//    }
//
//    fun facebookLoginStatus(
//        context: Context,
//        accessToken: String,
//        code: String
//    ): MutableLiveData<LoginStatusResponse> {
//        val connection = MutableLiveData<LoginStatusResponse>()
//        FacebookLoginRepository.instance.facebookLoginStatus(accessToken, code, object :
//            FacebookLoginStatusCallback {
//            override fun configurationSuccess(loginStatusResponse: LoginStatusResponse?) {
//                connection.postValue(loginStatusResponse)
//            }
//
//            override fun configurationFailure(message: String) {
//            }
//        })
//        return connection
//    }
//
//    fun getFacebookUserProfile(
//        context: Context,
//        fields: String,
//        accessToken: String
//    ): MutableLiveData<FacebookUserProfileResponse> {
//        val connection = MutableLiveData<FacebookUserProfileResponse>()
//        FacebookLoginRepository.instance.getFacebookUserProfile(fields, accessToken, object :
//            FacebookUserProfileCallback {
//            override fun configurationSuccess(facebookUserProfileResponse: FacebookUserProfileResponse?) {
//                connection.postValue(facebookUserProfileResponse)
//            }
//
//            override fun configurationFailure(message: String) {
//            }
//        })
//        return connection
//    }
//
//    fun getFbLogin(context: Context, fbLoginRequest: FbLoginRequest): LiveData<LoginResponseModel> {
//        val loginResponseLiveData: MutableLiveData<LoginResponseModel> = MutableLiveData()
//        val loginResponseModel = LoginResponseModel()
//        val requestParam = JsonObject()
//        requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbLoginRequest.fbId)
//        requestParam.addProperty(AppConstants.API_PARAM_NAME, fbLoginRequest.name)
//        requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, fbLoginRequest.emailId)
//        requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbLoginRequest.accessToken)
//        requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, fbLoginRequest.fbMail)
//        requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, fbLoginRequest.profilePicUrl)
//        BaseCategoryServices.instance.fbLoginService(requestParam, object :
//            LoginCallBack {
//
//            override fun success(
//                status: Boolean,
//                loginResponse: Response<com.make.userManagement.bean.LoginResponse.LoginResponseModel>
//            ) {
//                if (status) {
//                    if (loginResponse.code() == 500) {
//                        loginResponseModel.responseCode = loginResponse.code()
//                        loginResponseModel.setDebugMessage(
//                            context.getString(R.string.something_went_wrong)
//                        )
//                        loginResponseLiveData.postValue(loginResponseModel)
//                    } else if (loginResponse.code() == 400 || loginResponse.code() == 401 || loginResponse.code() == 404 || loginResponse.code() == 409) {
//                        val errorObject =
//                            JSONObject(loginResponse.errorBody()?.string())
//                        loginResponseModel.responseCode = errorObject.getInt("responseCode")
//                        var debugMessage = ""
//                        when (loginResponseModel.responseCode) {
//                            4002 -> {
//                                debugMessage =
//                                    context.getString(R.string.username_password_doest_match)
//                            }
//                            4003 -> {
//                                debugMessage = context.getString(R.string.password_cannot_be_blank)
//                            }
//                            4004 -> {
//                                debugMessage = context.getString(R.string.please_provide_valid_name)
//                            }
//                            4005 -> {
//                                debugMessage = context.getString(R.string.email_id_cannot_be_blank)
//                            }
//                            4006 -> {
//                                debugMessage = context.getString(R.string.user_can_not_login)
//                            }
//                            4103 -> {
//                                debugMessage = context.getString(R.string.user_deactivated)
//                            }
//                            4401 -> {
//                                debugMessage = context.getString(R.string.user_does_not_exists)
//                            }
//                            else -> {
//                                if (loginResponse.body()?.getDebugMessage() == null || loginResponse.body()?.getDebugMessage().isNullOrEmpty())
//                                    debugMessage = context.getString(R.string.something_went_wrong)
//                                else
//                                    debugMessage = loginResponse.body()?.getDebugMessage().toString()
//                            }
//                        }
//                        loginResponseModel.isSuccessful = false
//                        loginResponseModel.debugMessage = debugMessage
//                        loginResponseLiveData.postValue(loginResponseModel)
//                    } else if (loginResponse.body() != null) {
//                        UserPreference.instance.userAuthToken =
//                            loginResponse.headers().get("x-auth")
//                        val gson = Gson()
//                        val tmp = gson.toJson(loginResponse.body())
//                        val loginResponseModel = gson.fromJson(tmp, LoginResponseModel::class.java)
//                        loginResponseModel.isSuccessful = true
//                        loginResponseLiveData.postValue(loginResponseModel)
//                        FirebaseAnalytics.getInstance(context)
//                            .setUserId(loginResponseModel.data?.id.toString())
//                    } else {
//                        loginResponseModel.isSuccessful = false
//                        loginResponseModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        loginResponseLiveData.postValue(loginResponseModel)
//                    }
//                } else {
//                    loginResponseModel.responseCode = loginResponse.code()
//                    loginResponseModel.isSuccessful = false
//                    loginResponseModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    loginResponseLiveData.postValue(loginResponseModel)
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                loginResponseModel.isSuccessful = false
//                loginResponseLiveData.postValue(loginResponseModel)
//            }
//        })
//        return loginResponseLiveData
//    }
}