package panteao.make.ready.dependencies.providers

import android.content.Context
import android.content.SharedPreferences
import panteao.make.ready.utils.constants.SharedPrefesConstants.DTG_SHARED_PREFS

class DTGPrefrencesProvider(mContext: Context) {
    var dtgSharePrefs: SharedPreferences = mContext.getSharedPreferences(DTG_SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveExpiryDays(expiryDays: Int) {
        val editor = dtgSharePrefs.edit()
        editor.putInt("expiryDays", expiryDays)
        editor.apply()
    }

    fun getExpiryDays(): Int {
        return dtgSharePrefs.getInt("expiryDays", 0)
    }
}