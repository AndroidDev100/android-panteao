package panteao.make.ready.utils.helpers.database.preferences

import panteao.make.ready.PanteaoApplication
import panteao.make.ready.utils.helpers.database.BaseAppPreferences


class UserPreference private constructor() :
    BaseAppPreferences(
        PanteaoApplication.getInstance()?.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE)
    ) {

    val TAG = UserPreference::class.java.simpleName

    companion object {

        private val SHARED_PREF_NAME = "user_pref_enveu"
        private val PRIVATE_MODE = 0
        private var mInstance: UserPreference? = null

        //Single Instance
        val instance: UserPreference
            get() {
                if (mInstance == null) {
                    synchronized(UserPreference::class.java) {
                        if (mInstance == null) {
                            mInstance = UserPreference()
                        }
                    }
                }
                return mInstance as UserPreference
            }
    }

    /**
     * Keys
     */
    private interface Keys {
        companion object {
            val SELECTED_VIDEO_QUALITY = "video_quality"
            val SELECTED_LANGUAGE_POSITION = "selected_language_position"
            val USER_NAME = "user_name"
            val USER_EMAIL_ID = "user_email_id"
            val USER_PROFILE_PIC = "user_profile_pic"
            val USER_MOB_NO = "user_mob_no"
            val USER_GENDER = "user_gender"
            val USER_DOB = "user_dob"
            val USER_AUTH_TOKEN = "user_auth_token"
            val IS_LOGIN = "is_login"
            val LOGIN_TYPE = "login_type"
            val SELECTED_LANGUAGE = "selected_language"
            val FB_ACCESS_TOKEN = "fb_access_token"
            val ENTITLEMENT_STATUS = "entitlement_status"
            val APP_PREF_ACCESS_TOKEN = "accesstoken"
        }
    }

    /**
     * Get/Set Login value into BaseAppPreferences
     * @param isLogin
     */
    var isLogin: Boolean
        get() = getBoolean(Keys.IS_LOGIN, false)
        set(isLogin) = setBoolean(Keys.IS_LOGIN, isLogin)

    /**
     * Get/Set Login Type value into BaseAppPreferences
     * @param loginType
     */
    var loginType: String?
        get() = getString(Keys.LOGIN_TYPE, "")
        set(loginType) = setString(Keys.LOGIN_TYPE, loginType)

    /**
     * Get/Set Authorization value into BaseAppPreferences
     * @param authorizationToken
     */
    var userAuthToken: String?
        get() = getString(Keys.USER_AUTH_TOKEN, "")
        set(authorizationToken) = setString(Keys.USER_AUTH_TOKEN, authorizationToken)

    /**
     * Get/Set username into BaseAppPreferences
     * @param username
     */
    var userName: String?
        get() = getString(Keys.USER_NAME, "")
        set(username) = setString(Keys.USER_NAME, username)

    /**
     * Get/Set Profile pic into BaseAppPreferences
     */
    var profilePic: String?
        get() = getString(Keys.USER_PROFILE_PIC, "")
        set(profilePic) = setString(Keys.USER_PROFILE_PIC, profilePic)

    /**
     * Get/Set User Email Id into BaseAppPreferences
     */
    var userEmailId: String?
        get() = getString(Keys.USER_EMAIL_ID, "")
        set(useremailid) = setString(Keys.USER_EMAIL_ID, useremailid)

    /**
     * Get/Set User Mobile Number into BaseAppPreferences
     */
    var userMobNo: String?
        get() = getString(Keys.USER_MOB_NO, "")
        set(usermobno) = setString(Keys.USER_MOB_NO, usermobno)

    /**
     * Get/Set User Gender into BaseAppPreferences
     */
    var userGender: String?
        get() = getString(Keys.USER_GENDER, "")
        set(usergender) = setString(Keys.USER_GENDER, usergender)

    /**
     * Get/Set User Date Of Birth into BaseAppPreferences
     */
    var userDob: Long
        get() = getLong(Keys.USER_DOB, 0L)
        set(userdob) = setLong(Keys.USER_DOB, userdob)

    /**
     * Get/Set selected_languge into BaseAppPreferences
     * @param selectedLanguage
     */
    var selectedLanguage: String?
        get() = getString(Keys.SELECTED_LANGUAGE, "Thai")
        set(selectedLanguage) = setString(Keys.SELECTED_LANGUAGE, selectedLanguage)

    /**
     * Get/Set selected_languge into BaseAppPreferences
     * @param selectedLanguagePosition
     */
    var selectedLanguagePosition: Int
        get() = getInt(Keys.SELECTED_LANGUAGE_POSITION, 0)
        set(selectedLanguage) = setInt(Keys.SELECTED_LANGUAGE_POSITION, selectedLanguage)

    /**
     * Get/Set User Date Of Birth into BaseAppPreferences
     */
    var fbAccessToken: String?
        get() = getString(Keys.FB_ACCESS_TOKEN, "")
        set(fbAccessToken) = setString(Keys.FB_ACCESS_TOKEN, fbAccessToken)

    var entitlementState: Boolean
        get() = getBoolean(Keys.ENTITLEMENT_STATUS, false)
        set(fbAccessToken) = setBoolean(Keys.ENTITLEMENT_STATUS, fbAccessToken)

    var xAuthToken: String?
        get() = getString(Keys.APP_PREF_ACCESS_TOKEN, "")
        set(authorizationToken) = setString(Keys.USER_AUTH_TOKEN, authorizationToken)
    var selectedVideoQuality: Int?
        get() = getInt(Keys.SELECTED_VIDEO_QUALITY, 0)
        set(selectedLanguage) = setInt(Keys.SELECTED_VIDEO_QUALITY, selectedLanguage!!)

    override fun clear() {
        super.clear()
    }

}