package panteao.make.ready.activities.usermanagment.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.homeactivity.ui.TVHomeActivity
import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse
import panteao.make.ready.callbacks.commonCallbacks.CustomTextChangeListener
import panteao.make.ready.callbacks.commonCallbacks.UserAuthenticationCodeCallback
import panteao.make.ready.databinding.ActivityLoginBinding
import panteao.make.ready.fragments.AuthenticationCodeDialogFragment
import panteao.make.ready.fragments.ResponseDialogFragment
import panteao.make.ready.fragments.UserProfileDialogFragment
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.repository.userManagement.FbLoginManager
import panteao.make.ready.repository.userManagement.FbLoginRequest
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.Utils
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.SharedPrefHelper
import panteao.make.ready.utils.helpers.StringUtils
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import java.net.HttpURLConnection
import java.util.regex.Pattern

class TVLoginActivity : TvBaseBindingActivity<ActivityLoginBinding>(), View.OnClickListener,
    UserAuthenticationCodeCallback,
    NoInternetFragment.OnFragmentInteractionListener {


    private val handler = Handler()
    private val TAG = TVLoginActivity::class.java.simpleName
    private val FB_FORCE_LOGIN_REQ_CODE = 5000
    private lateinit var activityLoginBinding: ActivityLoginBinding

    private var authenticationCodeDialogFragment: AuthenticationCodeDialogFragment? = null
    private var userProfileDialogFragment: UserProfileDialogFragment? = null
    private var responseDialogFragment: ResponseDialogFragment? = null
    private var noInternetFragment = NoInternetFragment()
    private var mAuthenticationCodeListener: AuthenticationCodeListener? = null
    private var count = 0
    override fun onStop() {
        handler.removeCallbacksAndMessages(null)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserPreference.instance.clear()
        SharedPrefHelper(this).clear()
        activityLoginBinding = binding
        connectionObserver()
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(boolean: Boolean) {
        if (boolean) {
            activityLoginBinding.etEmail.requestFocus()
            uiIntialization()
        } else {
            addFragment(
                noInternetFragment,
                android.R.id.content,
                true,
                AppConstants.TAG_NO_INTERNET_FRAGMENT
            )
        }
    }

    private fun uiIntialization() {
        activityLoginBinding.btnForgetPassword.setOnClickListener(this)
        activityLoginBinding.rlFacebookLogin.setOnClickListener(this)
        activityLoginBinding.llLogin.setOnClickListener(this)
    }

    override fun onFragmentInteraction() {
        if (isOnline(this)) {
            hideFragment(noInternetFragment, AppConstants.TAG_NO_INTERNET_FRAGMENT)
            connectionObserver()
        } else {
            Toast.makeText(this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show()
        }
    }


    private fun setTextChangeListeners() {
        activityLoginBinding.etEmail.addTextChangedListener(object :
            CustomTextChangeListener(object : TextChangeListener {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length < 4) {
                        activityLoginBinding.errorEmail.visibility = View.VISIBLE
                    } else {
                        activityLoginBinding.errorEmail.visibility = View.INVISIBLE
                    }
                }
            }) {})
    }

    private fun validateCredentials(email: String, password: String): Boolean {

        if (StringUtils.isNullOrEmptyOrZero(email)) {
            activityLoginBinding.errorEmail.text =
                resources.getString(R.string.email_id_cannot_be_blank)
            activityLoginBinding.errorEmail.visibility = View.VISIBLE
            return false
        }

        if (!Utils.isValidEmailAddress(email)) {
            activityLoginBinding.errorEmail.text = resources.getString(R.string.valid_email)
            activityLoginBinding.errorEmail.visibility = View.VISIBLE
            return false
        }

        if (StringUtils.isNullOrEmptyOrZero(password)) {
            activityLoginBinding.errorPassword.text =
                resources.getString(R.string.password_cannot_be_blank)
            activityLoginBinding.errorPassword.visibility = View.VISIBLE
            return false
        }
        return passwordCheck(password)
    }

    private fun passwordCheck(password: String): Boolean {
        val passwordRegex = "^[A-Za-z0-9\\d!&^%$#@()\\_+-]{6,20}$"
        var check = false
        val mPattern = Pattern.compile(passwordRegex)
        val matcher = mPattern.matcher(password)
        if (!matcher.find()) {
            activityLoginBinding.errorPassword.visibility = View.VISIBLE
            activityLoginBinding.errorPassword.text =
                resources.getString(R.string.strong_password_required)
        } else {
            activityLoginBinding.errorPassword.visibility = View.INVISIBLE
            check = true
        }
        return check
    }

    private fun callUserLoginApi(email: String, password: String) {
        activityLoginBinding.progressBar.visibility = View.VISIBLE
        RegistrationLoginRepository.instance?.getLoginAPIResponse(
            this@TVLoginActivity,
            email,
            password
        )
            ?.observe(this, Observer {
                if (it.isSuccessful) {
                    activityLoginBinding.progressBar.visibility = View.GONE
                    UserPreference.instance.isLogin = true
                    UserPreference.instance.loginType = AppConstants.MANUAL_LOGIN
                    UserPreference.instance.userName = it.data.name
                    UserPreference.instance.userEmailId = it.data.email
                    UserPreference.instance.userProfile = Gson().toJson(
                        it.data
                    )
                    startActivity(Intent(this@TVLoginActivity, TVHomeActivity::class.java))
                    finish()
                } else {
                    val bundle = Bundle()
                    bundle.putString(AppConstants.ERROR_MESSAGE, it.debugMessage)
                    responseDialogFragment = ResponseDialogFragment.newInstance()
                    responseDialogFragment?.arguments = bundle
                    responseDialogFragment?.show(
                        supportFragmentManager,
                        AppConstants.TAG_FRAGMENT_ALERT
                    )
                    activityLoginBinding.progressBar.visibility = View.GONE
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnForgetPassword -> {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
            }
            R.id.rlFacebookLogin -> {
                callFacebookLoginApi()
            }
            R.id.llLogin -> {
                val email = activityLoginBinding.etEmail.text.toString().trim()
                val password = activityLoginBinding.etPassword.text.toString().trim()
                if (validateCredentials(email, password)) {
                    Utils.hideSoftKeyboard(activityLoginBinding.rootView)
                    callUserLoginApi(email, password)
                }
            }
        }
    }

    override fun userAuthenticationCode(code: String) {
        callLoginStatusApi(code)
    }

    private fun callFacebookLoginApi() {
        authenticationCodeDialogFragment = AuthenticationCodeDialogFragment.newInstance()
        authenticationCodeDialogFragment?.isCancelable = false
        authenticationCodeDialogFragment?.show(
            supportFragmentManager,
            AppConstants.TAG_FRAGMENT_ALERT
        )

        count++
        if (count == 3) {
            authenticationCodeDialogFragment?.dismiss()
            count = 0
            return
        }
        RegistrationLoginRepository.instance?.facebookLoginApi(
            this@TVLoginActivity,
            AppConstants.FACEBOOK_ID_ACCESS_TOKEN,
            AppConstants.SCOPE
        )?.observe(this, Observer {
            if (it != null) {
                getAuthenticationCodeListener()?.userAuthenticationCode(it, true)
                it.expiresIn?.times(1000)?.toLong()?.let { it1 ->
                    handler.postDelayed(Runnable {
                        if (count < 3) {
                            callFacebookLoginApi()
                        }
                    }, it1)
                }
            } else {
                Toast.makeText(
                    this@TVLoginActivity,
                    getString(R.string.player_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun callLoginStatusApi(code: String?) {
        code?.let { it1 ->
            RegistrationLoginRepository.instance?.facebookLoginStatus(
                this@TVLoginActivity,
                AppConstants.FACEBOOK_ID_ACCESS_TOKEN,
                it1
            )?.observe(this@TVLoginActivity, Observer {
                if (it != null) {
                    authenticationCodeDialogFragment?.cancelTimer()
                    callUserProfileApi(it.accessToken)
                } else {
                    Toast.makeText(
                        this@TVLoginActivity,
                        getString(R.string.player_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun callUserProfileApi(accessToken: String?) {
        accessToken?.let { it1 ->
            RegistrationLoginRepository.instance?.getFacebookUserProfile(
                this@TVLoginActivity,
                AppConstants.FACEBOOK_FIELD,
                it1
            )?.observe(this, Observer {
                if (it != null) {
                    Log.d(TAG, it.toString())
                    UserPreference.instance.loginType = AppConstants.SOCIAL_LOGIN
                    UserPreference.instance.userName = it.firstName + " " + it.lastName
                    UserPreference.instance.userEmailId = it.email
                    UserPreference.instance.profilePic = it.picture?.data?.url
                    UserPreference.instance.fbAccessToken = accessToken

                    val fbLoginRequest = FbLoginRequest()
                    fbLoginRequest.fbId = it.id
                    fbLoginRequest.name = it.name
                    fbLoginRequest.emailId = it.email
                    fbLoginRequest.accessToken = UserPreference.instance.fbAccessToken
                    fbLoginRequest.fbMail = it.email.isNullOrEmpty()
                    fbLoginRequest.profilePicUrl = it.picture?.data?.url
                    FbLoginManager.getInstance().facebookUserProfileResponse = it
                    FbLoginManager.getInstance().fbLoginRequest = fbLoginRequest
                    callRegisterFacebookUserToServer(fbLoginRequest)
                }
            })
        }
    }

    private fun callRegisterFacebookUserToServer(fbLoginRequest: FbLoginRequest) {
        val name = fbLoginRequest.name
        val fbId = fbLoginRequest.fbId
        val emailId = fbLoginRequest.emailId
        val accessToken = fbLoginRequest.accessToken
        val fbMail = fbLoginRequest.fbMail
        val profilePicUrl = fbLoginRequest.profilePicUrl
        RegistrationLoginRepository.instance?.getFbLogin(this@TVLoginActivity, fbLoginRequest)
            ?.observe(this, Observer {
                if (it != null) {
                    if (authenticationCodeDialogFragment != null) {
                        authenticationCodeDialogFragment?.dismiss()
                    }
                    if (it.responseCode == 2000) {
                        UserPreference.instance.userEmailId = it.data?.email
                        userProfileDialogFragment = UserProfileDialogFragment.newInstance()
                        userProfileDialogFragment?.isCancelable = false
                        userProfileDialogFragment?.show(
                            supportFragmentManager,
                            AppConstants.TAG_FRAGMENT_ALERT
                        )
                    } else if (it.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Toast.makeText(this@TVLoginActivity, it.debugMessage, Toast.LENGTH_SHORT)
                            .show()
                    } else if (it.responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        Toast.makeText(this@TVLoginActivity, it.debugMessage, Toast.LENGTH_SHORT)
                            .show()
                    } else if (it.responseCode == HttpURLConnection.HTTP_CONFLICT) {
                        Toast.makeText(this@TVLoginActivity, it.debugMessage, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@TVLoginActivity, it.debugMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FB_FORCE_LOGIN_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val fbLoginRequest = FbLoginManager.getInstance().fbLoginRequest
                callRegisterFacebookUserToServer(fbLoginRequest)
            } else {
//                Toast.makeText(
//                    this@TVLoginActivity,
//                    getString(R.string.cancel_by_user),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        FbLoginManager.getInstance().clear()
    }

    public fun getAuthenticationCodeListener(): AuthenticationCodeListener? {
        return mAuthenticationCodeListener
    }

    public fun setAuthenticationCodeListener(listener: AuthenticationCodeListener) {
        this.mAuthenticationCodeListener = listener
    }

    interface AuthenticationCodeListener {
        fun userAuthenticationCode(facebookLoginResponse: FacebookLoginResponse, show: Boolean)
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(inflater)
    }
}
