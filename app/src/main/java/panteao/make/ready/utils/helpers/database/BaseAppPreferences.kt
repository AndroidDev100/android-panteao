package panteao.make.ready.utils.helpers.database

import android.content.SharedPreferences

/**
 * This class is wrapper class for handle preferences
 */
open class BaseAppPreferences(private val mPref: SharedPreferences?) {

    /**
     * Save String value into preferences
     * @param key
     * @param value
     */
    protected fun setString(key: String?, value: String?) {
        if (key != null && value != null) {
            try {
                if (mPref != null) {
                    val editor = mPref.edit()
                    editor.putString(key, value)
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }


    /**
     * Save Long value into preferences
     * @param key
     * @param value
     */
    protected fun setLong(key: String?, value: Long) {
        if (key != null) {
            try {
                if (mPref != null) {
                    val editor = mPref.edit()
                    editor.putLong(key, value)
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }

    /**
     * Save Int value into preferences
     * @param key
     * @param value
     */
    protected fun setInt(key: String?, value: Int) {
        if (key != null) {
            try {
                if (mPref != null) {
                    val editor = mPref.edit()
                    editor.putInt(key, value)
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }

    /**
     * Set Double value into preferences
     * @param key
     * @param value
     */
    protected fun setDouble(key: String?, value: Double) {
        if (key != null) {
            try {
                if (mPref != null) {
                    val editor = mPref.edit()
                    editor.putFloat(key, value.toFloat())
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }

    /**
     * Save Boolean value into preferences
     * @param key
     * @param value
     */
    protected fun setBoolean(key: String?, value: Boolean) {
        if (key != null) {
            try {
                if (mPref != null) {
                    val editor = mPref.edit()
                    editor.putBoolean(key, value)
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }

    /**
     * Get Int value from preferences
     * @param key
     * @param defaultValue
     * @return
     */
    protected fun getInt(key: String?, defaultValue: Int): Int {
        return if (mPref != null && key != null && mPref.contains(key)) {
            mPref.getInt(key, defaultValue)
        } else defaultValue
    }

    /**
     * Get Long value from preferences
     * @param key
     * @param defaultValue
     * @return
     */
    protected fun getLong(key: String?, defaultValue: Long): Long {
        return if (mPref != null && key != null && mPref.contains(key)) {
            mPref.getLong(key, defaultValue)
        } else defaultValue
    }

    /**
     * Get Boolean value from preferences
     * @param key
     * @param defaultValue
     * @return
     */
    protected fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return if (mPref != null && key != null && mPref.contains(key)) {
            mPref.getBoolean(key, defaultValue)
        } else defaultValue
    }

    /**
     * Get String value from preferences
     * @param key
     * @param defaultValue
     * @return
     */
    protected fun getString(key: String?, defaultValue: String): String? {
        return if (mPref != null && key != null && mPref.contains(key)) {
            mPref.getString(key, defaultValue)
        } else defaultValue
    }

    /**
     * Get Double value from preferences
     * @param key
     * @param defaultValue
     * @return
     */
    protected fun getDouble(key: String?, defaultValue: Double): Double {
        return if (mPref != null && key != null && mPref.contains(key)) {
            mPref.getFloat(key, defaultValue.toFloat()).toDouble()
        } else defaultValue
    }

    /**
     * Remove String value from preferences
     * @param key
     */
    protected fun removeString(key: String?) {
        if (key != null) {
            try {
                if (mPref != null && mPref.contains(key)) {
                    val editor = mPref.edit()
                    editor.remove(key)
                    editor.apply()
                }
            } catch (e: Exception) {
            }

        }
    }

    /**
     * This Method Clear shared preference.
     */
    open fun clear() {
        val editor = mPref?.edit()
        editor?.clear()
        editor?.apply()
    }

    companion object {

        private val TAG = BaseAppPreferences::class.java.name
    }
}
