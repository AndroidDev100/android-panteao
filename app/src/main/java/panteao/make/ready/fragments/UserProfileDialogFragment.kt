package panteao.make.ready.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import panteao.make.ready.R
import panteao.make.ready.activities.homeactivity.ui.HomeActivity
import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse
import panteao.make.ready.callbacks.commonCallbacks.UserAuthenticationCodeCallback
import panteao.make.ready.databinding.UserProfileDialogBinding
import panteao.make.ready.repository.userManagement.FbLoginManager
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher


class UserProfileDialogFragment : DialogFragment(), View.OnClickListener {

    private lateinit var userProfileDialogBinding: UserProfileDialogBinding
    private var facebookUserProfileResponse: FacebookUserProfileResponse? = null
    private var mListener: UserAuthenticationCodeCallback? = null
    private var requestOptions: RequestOptions? = null


    companion object {
        fun newInstance(): UserProfileDialogFragment {
            return UserProfileDialogFragment()
        }
    }

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
        userProfileDialogBinding = DataBindingUtil.inflate(inflater, R.layout.user_profile_dialog, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        facebookUserProfileResponse = FbLoginManager.getInstance().facebookUserProfileResponse
        return userProfileDialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfileDialogBinding.txtUserName.text = facebookUserProfileResponse?.name
        userProfileDialogBinding.btnContinue.setOnClickListener(this)

        context?.let {
            Glide
                .with(it)
                .load(facebookUserProfileResponse?.picture?.data?.url)
                .centerCrop()
                .placeholder(R.drawable.profile_icon)
                .into(userProfileDialogBinding.imgUserProfilePic)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnContinue -> {
                dismiss()
                activity?.applicationContext?.let {
                    this.activity?.let { it1 ->
                        UserPreference.instance.isLogin = true
                        ActivityLauncher(activity).homeScreen(it1, HomeActivity::class.java)
                    }
                }
                activity?.finish()
            }
        }
    }
}