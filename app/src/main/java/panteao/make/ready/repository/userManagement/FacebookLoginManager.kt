package panteao.make.ready.repository.userManagement

import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse
import panteao.make.ready.beanModel.facebook.loginstatusresponse.LoginStatusResponse
import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse
import panteao.make.ready.networking.apiendpoints.ApiInterceptor
import panteao.make.ready.networking.apiendpoints.ApiInterfaceFacebook
import panteao.make.ready.utils.constants.AppConstants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class FacebookLoginManager private constructor() {

    private val TAG = FacebookLoginManager::class.java.simpleName

    private var mApiInterface: ApiInterfaceFacebook? = null


    init {
    }

    companion object {
        private var mInstance: FacebookLoginManager? = null

        val instance: FacebookLoginManager
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = FacebookLoginManager()
                }
                return mInstance as FacebookLoginManager
            }
    }

    private fun createFacebookApiClient() {
        Log.d(TAG, "createFacebookClient")
        mApiInterface = getFacebookClient().create(ApiInterfaceFacebook::class.java)
    }

    private fun getFacebookClient(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(AppConstants.FACEBOOK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getHttpClient())
            .build()
    }

    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(ApiInterceptor())
        httpClient.addInterceptor(interceptor)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }


    ////////////////////////////////////////////////////////////////////
    /////////////     FACEBOOK APIS IMPLEMENTATIONS ////////////////////
    ////////////////////////////////////////////////////////////////////

    fun facebookLoginApi(accessToken: String, scope: String): Observable<FacebookLoginResponse>? {
        createFacebookApiClient()
        return mApiInterface?.facebookLoginApi(accessToken, scope)
    }

    fun facebookLoginStatus(accessToken: String, code: String): Observable<LoginStatusResponse>? {
        return mApiInterface?.facebookLoginStatus(accessToken, code)
    }

    fun getFacebookUserProfile(fields: String, accessToken: String): Observable<FacebookUserProfileResponse>? {
        return mApiInterface?.getFacebookUserProfile(fields, accessToken)
    }

}
