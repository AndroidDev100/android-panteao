package panteao.make.ready.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import panteao.make.ready.R
import panteao.make.ready.activities.usermanagment.ui.TVLoginActivity
import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse
import panteao.make.ready.callbacks.commonCallbacks.UserAuthenticationCodeCallback
import panteao.make.ready.databinding.AuthenticationCodeDialogBinding
import java.util.*


class AuthenticationCodeDialogFragment : DialogFragment() {

    private lateinit var authenticationCodeDialogBinding: AuthenticationCodeDialogBinding

    private var loginResponse: FacebookLoginResponse? = null
    private var mListener: UserAuthenticationCodeCallback? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimeLeftInMillis: Long = 0
    private var mInterval: Int? = 5

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as UserAuthenticationCodeCallback
    }

    override fun onStart() {
        if (dialog != null) {
            dialog?.window?.attributes.let { it }?.width = android.view.WindowManager.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes.let { it }?.height = android.view.WindowManager.LayoutParams.MATCH_PARENT
        }
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        authenticationCodeDialogBinding = DataBindingUtil.inflate(inflater, R.layout.authentication_code_dialog, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        (activity as TVLoginActivity).setAuthenticationCodeListener(object :
            TVLoginActivity.AuthenticationCodeListener{
            override fun userAuthenticationCode(
                facebookLoginResponse: FacebookLoginResponse, show: Boolean) {
                loginResponse = facebookLoginResponse
                updateFacebookCallbackResponse()
            }
        })
        return authenticationCodeDialogBinding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val first = getString(R.string.next_visit)
        val next = "<font color='#4080ff'>"+getString(R.string.facebook_com_device)+"</font>"
        authenticationCodeDialogBinding.txtNextVisit.text = (Html.fromHtml("$first $next"))
    }


    private fun updateFacebookCallbackResponse() {
        authenticationCodeDialogBinding.progressBar.visibility = View.GONE
        authenticationCodeDialogBinding.txtUserCode.text = loginResponse?.userCode
        authenticationCodeDialogBinding.txtUserCode.visibility = View.VISIBLE
        authenticationCodeDialogBinding.lnExpireTime.visibility = View.VISIBLE
        mTimeLeftInMillis = loginResponse?.expiresIn?.times(1000)?.toLong() ?: mTimeLeftInMillis
        mInterval = loginResponse?.interval ?: mInterval
        startTimer()
        authenticationCodeDialogBinding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun startTimer() {
        var intervalCount: Int = 0
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                intervalCount++

                if (intervalCount == mInterval) {
                    loginResponse?.code?.let { mListener?.userAuthenticationCode(it) }
                    intervalCount = 0
                }
                updateCountDownText()
            }
            override fun onFinish() {
                dismiss()
            }
        }.start()
    }

    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000) / 60
        val seconds = (mTimeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        authenticationCodeDialogBinding.txtCodeExpireIn.text = timeLeftFormatted
    }


    companion object {
        fun newInstance(): AuthenticationCodeDialogFragment {
            return AuthenticationCodeDialogFragment()
        }
    }

    override fun dismiss() {
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
        }
        super.dismiss()
    }

    fun cancelTimer() {
        mCountDownTimer?.cancel()
    }
}