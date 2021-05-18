package panteao.make.ready.tvBaseModels.basemodels

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

abstract class TvBaseBindingActivity<B : ViewDataBinding> : TVBaseActivity() {

    var binding: B? = null
        private set

    abstract fun inflateBindingLayout(inflater: LayoutInflater): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setupBinding(getLayoutInflater())
        setContentView(binding?.root)
    }


    private fun setupBinding(inflater: LayoutInflater): B {
        return inflateBindingLayout(inflater)
    }

    open fun updateLanguage(currentLanguage: String) {
        try {
            // String language=AppCommonMethod.getSystemLanguage();
            if (currentLanguage.equals("", ignoreCase = true)) {
                KsPreferenceKeys.getInstance().appLanguage = "English"
                KsPreferenceKeys.getInstance().appPrefLanguagePos = 0
                AppCommonMethod.updateLanguage("en", this)
            } else {
                if (KsPreferenceKeys.getInstance().appLanguage.equals("Thai", ignoreCase = true) || KsPreferenceKeys.getInstance().appLanguage.equals("हिंदी", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("en", this)
                } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("en", this)
                }
            }
        } catch (e: Exception) {
        }
    }

}
