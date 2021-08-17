package panteao.make.ready.networking.apiendpoints

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import java.net.HttpURLConnection

class ApiInterceptor : Interceptor {

    private val TAG = ApiInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        try {
            if (response.request.url.toString()
                    .contains(AppConstants.FB_LOGIN_CALL) && response.code == HttpURLConnection.HTTP_OK
            ) {
                UserPreference.instance.userAuthToken = response.header("x-auth-token")
                Log.d(TAG, " " + response.code)
                Log.d(TAG, " " + response.header("x-auth-token"))
            }
        } catch (ex: Throwable) {
        }
        return response
    }
}
