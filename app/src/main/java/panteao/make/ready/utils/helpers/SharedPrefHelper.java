package panteao.make.ready.utils.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.security.CryptUtil;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.security.CryptUtil;

public class SharedPrefHelper {
    private static final String PREF_FILE = "Session";
    private final SharedPreferences mSharedPreferences;
    private final SharedPreferences.Editor mEditor;
    private static final int MODE_PRIVATE = 0;
    private CryptUtil cryptUtil;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        cryptUtil = CryptUtil.getInstance();
    }

    @SuppressLint("CommitPrefEdits")
    public void clear() {
        mSharedPreferences.edit();
        mEditor.clear();
        mEditor.commit();
    }

   /* public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }*/

    public String getString(String key, String defValue) {
        String decryptedValue = cryptUtil.decrypt(mSharedPreferences.getString(key, defValue), AppConstants.MY_MVHUB_ENCRYPTION_KEY);
        if (decryptedValue == null || decryptedValue.equalsIgnoreCase("") || key.equalsIgnoreCase("DMS_Response")) {
            decryptedValue = mSharedPreferences.getString(key, defValue);
        }
        return decryptedValue;
    }

    public void setString(String key, String value) {
        String encryptedValue;
        encryptedValue = cryptUtil.encrypt(value, AppConstants.MY_MVHUB_ENCRYPTION_KEY);
        if (key.equalsIgnoreCase("DMS_Response") || value.equalsIgnoreCase("")) {
            mEditor.putString(key, value);
        } else {
            mEditor.putString(key, encryptedValue);
        }
        mEditor.commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    protected void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }


    public HashSet<String> getRecentSearchesMap() {
        return (HashSet<String>) mSharedPreferences.getStringSet("RecentSearchMap", null);
    }

    public void addRecentSearch(String searchKeyWord) {
        HashSet<String> recentSearchesMap = getRecentSearchesMap();
        if (recentSearchesMap == null) {
            recentSearchesMap = new HashSet();
            recentSearchesMap.add(searchKeyWord);
        } else {
            recentSearchesMap.add(searchKeyWord);
        }
        mEditor.putStringSet("RecentSearchMap", recentSearchesMap);
        mEditor.commit();
    }

    public List<String> getRecentSearches() {
        if (getRecentSearchesMap() == null)
            return new ArrayList();
        return new ArrayList(getRecentSearchesMap());
    }

    public void clearRecentSearches() {
        HashSet<String> recentSearchesMap = new HashSet<String>();
        mEditor.putStringSet("RecentSearchMap", recentSearchesMap);
        mEditor.commit();
    }
}
