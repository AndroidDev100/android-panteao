package panteao.make.ready.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.security.CryptUtil;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.security.CryptUtil;


public class AppPreference{
    private static final String APP_PREFS = "app_prefs";
    private static AppPreference AppPreference;
    private final SharedPreferences.Editor mEditor;
    private static final String RECENT_SEARCH_LIST = "recent_search_list";
    private final SharedPreferences mPreferences;
    private final CryptUtil cryptUtil;

    private AppPreference(Context context) {
         mPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        cryptUtil = CryptUtil.getInstance();
    }

    public static AppPreference getInstance(Context context) {
        if (AppPreference == null) {
            AppPreference = new AppPreference(context);
        }
        return AppPreference;
    }

    public void removeKey(String key) {
        mEditor.remove(key).apply();
    }

    public void clear() {
        mEditor.clear().commit();
        AppPreference = null;
    }

    public String getRecentSearchList() {
        return getString(RECENT_SEARCH_LIST, "");
    }

    public void setRecentSearchList(String fcmToken) {
        setString(RECENT_SEARCH_LIST, fcmToken);
    }
    public String getString(String key, String defValue) {
        String decryptedValue = cryptUtil.decrypt(mPreferences.getString(key, defValue), AppConstants.OTT_ENCRYPTION_KEY);
        if (decryptedValue == null || decryptedValue.equalsIgnoreCase("")||key.equalsIgnoreCase("DMS_Response")) {
            decryptedValue = mPreferences.getString(key, defValue);
        }
        return decryptedValue;
    }

    public void setString(String key, String value) {
        String encryptedValue;
        encryptedValue = cryptUtil.encrypt(value, AppConstants.OTT_ENCRYPTION_KEY);
        if (key.equalsIgnoreCase("DMS_Response")||value.equalsIgnoreCase("")) {
            mEditor.putString(key, value);
        } else {
            mEditor.putString(key, encryptedValue);
        }
        mEditor.commit();
    }
}

