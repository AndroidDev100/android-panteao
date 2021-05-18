package panteao.make.ready.cardlayout.cardview


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.databinding.LayoutChangeLanguageItemBinding
import panteao.make.ready.fragments.changelanguage.LanguageModel
import panteao.make.ready.fragments.streamingSettings.VideoQualityModel
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys


open class ChangeLanguageCardView(context: Context) : BaseCardView(context) {
    private var customMenuCardViewBinding: LayoutChangeLanguageItemBinding? = null
    private var mAttachedToWindow: Boolean = false

    init {
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true
        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_change_language_item, this, true)
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        mAttachedToWindow = false
        super.onDetachedFromWindow()
    }

    fun setRecentSearches(recentSearch:String) {
        customMenuCardViewBinding?.episodeTitle?.text = recentSearch
    }

    fun setLanguageModel(languageModel: LanguageModel) {
        customMenuCardViewBinding?.episodeTitle?.text = languageModel.languageName
        if (KsPreferenceKeys.getInstance().appLanguage != null) {
            Logger.e("LanguageId",languageModel.languageId + " "+KsPreferenceKeys.getInstance().getAppLanguage())
            if (languageModel.languageId == KsPreferenceKeys.getInstance().getAppLanguage()) {
                customMenuCardViewBinding?.btnSelected?.visibility = View.VISIBLE
            } else {
                customMenuCardViewBinding?.btnSelected?.visibility = View.GONE
            }
        }
    }

    fun setVideoQualityModel(videoQualityModel: VideoQualityModel) {
        customMenuCardViewBinding?.episodeTitle?.text = videoQualityModel.languageName
        if (videoQualityModel.languageId == KsPreferenceKeys.getInstance().videoQuality) {
            customMenuCardViewBinding?.btnSelected?.visibility = View.VISIBLE
        } else {
            customMenuCardViewBinding?.btnSelected?.visibility = View.GONE
        }
    }
}
