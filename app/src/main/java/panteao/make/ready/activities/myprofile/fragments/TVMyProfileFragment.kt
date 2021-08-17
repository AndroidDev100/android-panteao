package panteao.make.ready.activities.myprofile.fragments


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.homeactivity.ui.TVHomeActivity
import panteao.make.ready.activities.usermanagment.ui.TVLoginActivity
import panteao.make.ready.beanModel.responseModels.LoginResponse.UserData
import panteao.make.ready.databinding.FragmentMyProfileBinding
import panteao.make.ready.fragments.LogOutDialogFragment
import panteao.make.ready.repository.userManagement.RegistrationLoginRepository
import panteao.make.ready.tvBaseModels.basemodels.TVBaseActivity
import panteao.make.ready.utils.Utils
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class TVMyProfileFragment : Fragment(), View.OnClickListener {


    private lateinit var fragmentMyProfileBinding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMyProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        return fragmentMyProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMyProfileBinding.btnStartWatching.setOnClickListener(this)
        fragmentMyProfileBinding.btnLogout.setOnClickListener(this)
        fragmentMyProfileBinding.btnStartWatching.requestFocus()
        callUserProfileApi()
        uiInitialization()
    }

    private fun uiInitialization() {
        val userData =
            Gson().fromJson<UserData>(UserPreference.instance.userProfile, UserData::class.java)
        if (userData != null) {
            if (userData.name?.trim().equals("", true)) {
                fragmentMyProfileBinding.txtUserName.visibility = View.GONE
            } else {
                fragmentMyProfileBinding.txtUserName.text = UserPreference.instance.userName
                fragmentMyProfileBinding.txtUserName.visibility = View.VISIBLE
            }

            if (userData.email?.trim().equals("", true)) {
                fragmentMyProfileBinding.txtUserEmail.visibility = View.GONE
            } else {
                fragmentMyProfileBinding.txtUserEmail.text = UserPreference.instance.userEmailId
                fragmentMyProfileBinding.txtUserEmail.visibility = View.VISIBLE
            }

            if (userData.dateOfBirth == 0L) {
                fragmentMyProfileBinding.txtUserDob.visibility = View.GONE
            } else {
                val date = UserPreference.instance.userDob
                fragmentMyProfileBinding.txtUserDob.text =
                    date.let { Utils.getDateDDMMMYYYY(it) }
                fragmentMyProfileBinding.txtUserDob.visibility = View.VISIBLE
            }

            if (userData.phoneNumber?.trim().equals("", true) ||userData.phoneNumber?.trim().equals("NULL", true)) {
                fragmentMyProfileBinding.txtUserMobNo.visibility = View.GONE
            } else {
                fragmentMyProfileBinding.txtUserMobNo.text = UserPreference.instance.userMobNo
                fragmentMyProfileBinding.txtUserMobNo.visibility = View.VISIBLE
            }

            if (userData.gender?.trim().equals("", true)) {
                fragmentMyProfileBinding.gender.visibility = View.GONE
            } else {
                if (UserPreference.instance.userGender.equals(
                        AppConstants.GENDER_MALE,
                        ignoreCase = true
                    )
                ) {
                    fragmentMyProfileBinding.gender.text = AppConstants.GENDER_MALE
                    fragmentMyProfileBinding.gender.visibility = View.VISIBLE
                } else if (UserPreference.instance.userGender.equals(
                        AppConstants.GENDER_FEMALE,
                        ignoreCase = true
                    )
                ) {
                    fragmentMyProfileBinding.gender.text = AppConstants.GENDER_FEMALE
                    fragmentMyProfileBinding.gender.visibility = View.VISIBLE
                }
            }

            fragmentMyProfileBinding.ivProfilePic.text =
                AppCommonMethod.getUserName(userData.name)

//        if (UserPreference.instance.profilePic != null && !UserPreference.instance.profilePic.equals(
//                ""
//            )
//        ) {
//            if (UserPreference.instance.profilePic!!.contains("http")) {
//                context?.let {
//                    Glide
//                        .with(it)
//                        .load(UserPreference.instance.profilePic)
//                        .centerCrop()
//                        .placeholder(R.drawable.profile_icon)
//                        .into(fragmentMyProfileBinding.imgUserProfilePic)
//                }
//                fragmentMyProfileBinding.ivProfilePic.visibility = View.GONE
//            } else {
//                context?.let {
//                    Glide
//                        .with(it)
//                        .load(
//                            SDKConfig.getInstance().cloundaryUrl + SDKConfig.getInstance().profileFolder + UserPreference.instance.profilePic
//                        )
//                        .centerCrop()
//                        .placeholder(R.drawable.profile_icon)
//                        .into(fragmentMyProfileBinding.imgUserProfilePic)
//                }
//                fragmentMyProfileBinding.ivProfilePic.visibility = View.GONE
//            }
//        } else {
//            fragmentMyProfileBinding.imgUserProfilePic.visibility = View.GONE
//        }
        }
    }

    private fun callUserProfileApi() {
        val token: String = KsPreferenceKeys.getInstance().appPrefAccessToken
        activity?.let { activity ->
            token?.let { token ->
                RegistrationLoginRepository.instance?.getUserProfile(activity, token)
                    ?.observe(activity, {
                        if (it.status) {
                            UserPreference.instance.let { userPref ->
                                userPref.userName = it.data?.name
                                userPref.userEmailId = it.data?.email
                                userPref.userMobNo = it.data?.phoneNumber.toString()
                                if (it.data.dateOfBirth != null)
                                    userPref.userDob = (it.data?.dateOfBirth as Long?)!!
                                userPref.userGender = it.data?.gender.toString()
                                userPref.profilePic = it.data?.profilePicURL.toString()
                                userPref.userProfile = Gson().toJson(
                                    it.data
                                )
                            }
                            uiInitialization()
                        } else {
                            startActivity(Intent(getActivity(), TVLoginActivity::class.java));
                            getActivity()?.finish();
                        }
                    })
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentMyProfileBinding.btnStartWatching -> {
                activity?.applicationContext?.let {
                    this.activity?.let { it1 ->
                        startActivity(Intent(activity, TVHomeActivity::class.java))
                        activity?.finish()
                    }
                }
            }
            fragmentMyProfileBinding.btnLogout -> {
                activity?.applicationContext?.let {
                    this.activity?.let { it1 ->
                        logOutUser()
                    }
                }
            }
        }
    }

    private fun changePassword() {

    }

    private fun logOutUser() {
        if (activity?.supportFragmentManager?.findFragmentByTag("ExitFragment") == null) {
            LogOutDialogFragment().view?.setBackgroundColor(Color.BLACK)
            (activity as TVBaseActivity).addFragment(
                LogOutDialogFragment(),
                android.R.id.content,
                false,
                "ExitFragment"
            )
        } else {
            activity?.supportFragmentManager?.findFragmentByTag("ExitFragment")?.let {
                activity?.supportFragmentManager?.beginTransaction()?.remove(it)?.commit()
            }
        }
    }

    companion object {

    }
}