package panteao.make.ready.repository.userManagement

import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.vipa.app.networking.ErrorMsgResponse
import panteao.make.ready.callbacks.facebookcallbacks.FacebookLoginCallback
import panteao.make.ready.callbacks.facebookcallbacks.FacebookLoginStatusCallback
import panteao.make.ready.callbacks.facebookcallbacks.FacebookUserProfileCallback
import retrofit2.HttpException

class FacebookLoginRepository {

    private val TAG = FacebookLoginRepository::class.java.simpleName

    companion object {

        private var mInstance: FacebookLoginRepository? = null

        val instance: FacebookLoginRepository
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = FacebookLoginRepository()
                }
                return mInstance as FacebookLoginRepository
            }
    }

    private fun parseErrorMessage(e: HttpException) : String? {
        val jsonString = (e as HttpException).response()?.errorBody()?.string()
        val errorMsgResponse = Gson().fromJson(jsonString, ErrorMsgResponse::class.java)
        return errorMsgResponse.debugMessage
    }

    fun facebookLoginApi(accessToken: String, scope: String, callback: FacebookLoginCallback) {
        val mDisposable = CompositeDisposable()
        FacebookLoginManager.instance.facebookLoginApi(accessToken, scope)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ loginResponse ->
                if (loginResponse.error == null) {
                    callback.configurationSuccess(loginResponse)
                } else {
                    callback.configurationFailure(loginResponse?.error.message.toString())
                }
            }) { e ->
                callback.configurationFailure(e.message.toString())
            }?.let { mDisposable.add(it) }
    }

    fun facebookLoginStatus(accessToken: String, code: String, callback: FacebookLoginStatusCallback) {
        val mDisposable = CompositeDisposable()
        FacebookLoginManager.instance.facebookLoginStatus(accessToken, code)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ loginStatusResponse ->
                if (loginStatusResponse.error == null) {
                    callback.configurationSuccess(loginStatusResponse)
                } else {
                    callback.configurationFailure(loginStatusResponse?.error.message.toString())
                }
            }) { e ->
                callback.configurationFailure(e.message.toString())
            }?.let { mDisposable.add(it) }
    }

    fun getFacebookUserProfile(fields: String, accessToken: String, callback: FacebookUserProfileCallback) {
        val mDisposable = CompositeDisposable()
        FacebookLoginManager.instance.getFacebookUserProfile(fields, accessToken)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ facebookUserProfileResponse ->
                if (facebookUserProfileResponse.error == null) {
                    callback.configurationSuccess(facebookUserProfileResponse)
                } else {
                    callback.configurationFailure(facebookUserProfileResponse?.error.message.toString())
                }
            }) { e ->
                callback.configurationFailure(e.message.toString())
            }?.let { mDisposable.add(it) }
    }


}

