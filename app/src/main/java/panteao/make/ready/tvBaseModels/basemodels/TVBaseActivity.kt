package panteao.make.ready.tvBaseModels.basemodels


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.*

open class TVBaseActivity : FragmentActivity() {

    internal var tabletSize: Boolean = false
    internal var logoutBackpress = false
    internal var currentLanguage = ""

    fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun showHideProgress(progressBar: ProgressBar) {
        showLoading(progressBar, false)
        val mHandler = Handler()
        mHandler.postDelayed({ dismissLoading(progressBar) }, 3000)

    }

    protected fun showLoading(progressBar: ProgressBar, `val`: Boolean) {
        if (`val`) {
            progressBar.visibility = View.VISIBLE
            progressBar.bringToFront()
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }


    fun dismissLoading(progressBar: ProgressBar?) {
        if (progressBar != null) {
            progressBar.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        currentLanguage = UserPreference.instance.selectedLanguage!!
//        if (UserPreference.instance.selectedLanguage.equals("Hindi", ignoreCase = true)) {
//            AppCommonMethod.updateLanguage("hi", this)
//        } else if (UserPreference.instance.selectedLanguage.equals("English", ignoreCase = true)) {
//            AppCommonMethod.updateLanguage("en", this)
//        } else if (UserPreference.instance.selectedLanguage.equals("Indonesia", ignoreCase = true)) {
//            AppCommonMethod.updateLanguage("in", this)
//        }
    }

    private fun checkAutoRotation() {
        if (Settings.System.getInt(applicationContext.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction.replace(android.R.id.content, fragment).addToBackStack(null)
        } else {
            fragmentTransaction.replace(android.R.id.content, fragment)
        }
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment, layout: Int, addToBackStack: Boolean, tag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (addToBackStack)
            fragmentTransaction.replace(layout, fragment, tag).addToBackStack(null)
        else
            fragmentTransaction.replace(layout, fragment, tag)
        fragmentTransaction.commit()
    }

    fun addFragment(
        fragment: Fragment,
        layout: Int,
        addToBackStack: Boolean,
        tag: String,
        isAdded: Boolean
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(layout, fragment, tag)
        fragmentTransaction.commit()
    }

    fun showFragment(fragment: Fragment, FRAGMENT_TAG: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) != null) {
            fragmentTransaction.show(fragment)
        }
        fragmentTransaction.commit()
    }

    fun hideFragment(fragment: Fragment, FRAGMENT_TAG: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) != null) {
            fragmentTransaction.hide(fragment)
        }
        fragmentTransaction.commit()
    }

    fun addToBackStackAndAdd(
        fragmentContainerId: Int,
        fragment: Fragment,
        tag: String,
        enterAnimId: Int,
        exitAnimId: Int
    ) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
            .add(fragmentContainerId, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun addFragment(
        fragmentContainerId: Int,
        fragment: Fragment,
        tag: String,
        enterAnimId: Int,
        exitAnimId: Int
    ) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, exitAnimId)
            .add(fragmentContainerId, fragment, tag)
            .commit()
    }

    fun replaceFragment(
        fragmentContainerId: Int, fragment: Fragment, tag: String?,
        enterAnimId: Int, exitAnimId: Int
    ) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, exitAnimId)
            .replace(fragmentContainerId, fragment, tag).commit()
    }

    fun popBackStackAndReplace(
        fragmentContainerId: Int, fragment: Fragment,
        tag: String?, enterAnimId: Int, exitAnimId: Int
    ) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, exitAnimId)
            .replace(fragmentContainerId, fragment, tag).commit()
    }

    fun addToBackStackAndReplace(
        fragmentContainerId: Int, fragment: Fragment,
        tag: String?, enterAnimId: Int, exitAnimId: Int
    ) {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
            .replace(fragmentContainerId, fragment, tag).commit()
    }

    fun clearBackStackAndReplace(
        fragmentContainerId: Int, fragment: Fragment,
        tag: String?, enterAnimId: Int, exitAnimId: Int
    ) {
        clearBackStack()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, exitAnimId)
            .replace(fragmentContainerId, fragment, tag).commit()
    }

    fun clearBackStack() {
        val fm = supportFragmentManager

        if (fm.backStackEntryCount > 0) {
            val first = fm.getBackStackEntryAt(0)
            fm.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun isOnline(activity: Context): Boolean {

        val cm = activity
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo?

        try {
            netInfo = Objects.requireNonNull(cm).activeNetworkInfo
            if (netInfo != null && netInfo.isConnectedOrConnecting) {
                return true
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return false
    }

    protected fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(view)
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Objects.requireNonNull(imm).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
